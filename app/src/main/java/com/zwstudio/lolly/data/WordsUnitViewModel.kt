package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.DictNote
import com.zwstudio.lolly.domain.UnitWord
import com.zwstudio.lolly.restapi.RestUnitWord
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean
import java.net.URLEncoder

@EBean
class WordsUnitViewModel : BaseViewModel2() {

    var lstWords = mutableListOf<UnitWord>()
    var isSwipeStarted = false
    var noteFromIndex = 0
    var noteToIndex = 0
    var noteIfEmpty = true
    val noteSite: DictNote?
        get() = vmSettings.selectedDictNote

    fun getData() =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .map { lstWords = it.lst!!.toMutableList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateSeqNum(id: Int, seqnum: Int) =
        retrofitJson.create(RestUnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateNote(id: Int, note: String) =
        retrofitJson.create(RestUnitWord::class.java)
            .updateNote(id, note)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun update(id: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, word: String, note: String) =
        retrofitJson.create(RestUnitWord::class.java)
            .update(id, textbookid, unit, part, seqnum, word, note)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun create(textbookid: Int, unit: Int, part: Int, seqnum: Int, word: String, note: String) =
        retrofitJson.create(RestUnitWord::class.java)
            .create(textbookid, unit, part, seqnum, word, note)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun delete(id: Int) =
        retrofitJson.create(RestUnitWord::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstWords.size) {
            val item = lstWords[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            }
        }
    }

    fun newUnitWord() = UnitWord().apply {
        textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstWords.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

    fun getNote(index: Int): Observable<Int> {
        val noteSite = noteSite ?: return Observable.empty()
        val item = lstWords[index]
        val url = noteSite.url!!.replace("{0}", URLEncoder.encode(item.word, "UTF-8"))
        return getHtml(url).concatMap {
            Log.d("", it)
            val result = extractTextFrom(it, noteSite.transformMac!!, noteSite.template!!) { _,_ -> "" }
            item.note = result
            updateNote(item.id, result)
        }
    }

    fun getNotes(ifEmpty: Boolean, onNext: (Int) -> Unit) {
        val noteSite = noteSite ?: return
        noteFromIndex = 0; noteToIndex = lstWords.size; noteIfEmpty = ifEmpty
        onNext(noteSite.wait!!)
    }

    fun getNextNote(onNextRow: (Int) -> Unit, onNext: () -> Unit) {
        if (noteIfEmpty)
            while (noteFromIndex < noteToIndex && !lstWords[noteFromIndex].note.isNullOrEmpty())
                noteFromIndex++
        if (noteFromIndex >= noteToIndex)
            onNext()
        else {
            val i = noteFromIndex
            getNote(i).subscribe { onNextRow(i) }
            noteFromIndex++
        }
    }
}
