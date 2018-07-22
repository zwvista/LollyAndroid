package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.UnitPhrase
import com.zwstudio.lolly.restapi.RestUnitPhrase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class PhrasesUnitViewModel : BaseViewModel2() {

    var lstPhrases = mutableListOf<UnitPhrase>()
    var isSwipeStarted = false

    fun getData() =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .map { lstPhrases = it.lst!!.toMutableList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateSeqNum(id: Int, seqnum: Int) =
        retrofitJson.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun update(id: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, phrase: String, translation: String) =
        retrofitJson.create(RestUnitPhrase::class.java)
            .update(id, textbookid, unit, part, seqnum, phrase, translation)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun create(textbookid: Int, unit: Int, part: Int, seqnum: Int, phrase: String, translation: String) =
        retrofitJson.create(RestUnitPhrase::class.java)
            .create(textbookid, unit, part, seqnum, phrase, translation)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun delete(id: Int) =
        retrofitJson.create(RestUnitPhrase::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstPhrases.size) {
            val item = lstPhrases[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            }
        }
    }

    fun newUnitPhrase() = UnitPhrase().apply {
        textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstPhrases.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

}
