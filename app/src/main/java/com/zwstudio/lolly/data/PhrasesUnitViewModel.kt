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

    fun getData(onNext: () -> Unit) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                lstPhrases = it.lst!!.toMutableList()
                onNext()
            }
    }

    fun updateSeqNum(id: Int, seqnum: Int, onNext: () -> Unit) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun update(id: Int, unit: Int, part: Int, seqnum: Int, phrase: String, translation: String, onNext: () -> Unit) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .update(id, unit, part, seqnum, phrase, translation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun create(unit: Int, part: Int, seqnum: Int, phrase: String, translation: String, onNext: (Int) -> Unit) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .create(unit, part, seqnum, phrase, translation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext(it)
            }
    }

    fun delete(id: Int, onNext: () -> Unit) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstPhrases.size) {
            val item = lstPhrases[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            updateSeqNum(item.id, i) {
                onNext(i - 1)
            }
        }
    }

    fun newUnitPhrase(): UnitPhrase {
        val item = UnitPhrase()
        item.textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstPhrases.maxWith(compareBy({ it.unitpart }, { it.seqnum }))
        item.unit = maxItem?.unit ?: vmSettings.usunitto
        item.part = maxItem?.part ?: vmSettings.uspartto
        item.seqnum = (maxItem?.seqnum ?: 0) + 1
        return item
    }

}
