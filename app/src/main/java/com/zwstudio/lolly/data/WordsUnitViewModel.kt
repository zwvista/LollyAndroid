package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.NoteSite
import com.zwstudio.lolly.domain.UnitWord
import com.zwstudio.lolly.restapi.RestUnitWord
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
    val noteSite: NoteSite?
        get() = vmSettings.selectedNoteSite

    fun getData(onNext: () -> Unit) {
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                lstWords = it.lst!!.toMutableList()
                onNext()
            }
    }

    fun updateSeqNum(id: Int, seqnum: Int, onNext: () -> Unit) {
        retrofitJson.create(RestUnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun updateNote(id: Int, note: String, onNext: () -> Unit) {
        retrofitJson.create(RestUnitWord::class.java)
            .updateNote(id, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun update(id: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, word: String, note: String, onNext: () -> Unit) {
        retrofitJson.create(RestUnitWord::class.java)
            .update(id, textbookid, unit, part, seqnum, word, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun create(textbookid: Int, unit: Int, part: Int, seqnum: Int, word: String, note: String, onNext: (Int) -> Unit) {
        retrofitJson.create(RestUnitWord::class.java)
            .create(textbookid, unit, part, seqnum, word, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext(it)
            }
    }

    fun delete(id: Int, onNext: () -> Unit) {
        retrofitJson.create(RestUnitWord::class.java)
            .delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstWords.size) {
            val item = lstWords[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            updateSeqNum(item.id, i) {
                onNext(i - 1)
            }
        }
    }

    fun newUnitWord(): UnitWord {
        val item = UnitWord()
        item.textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstWords.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        item.unit = maxItem?.unit ?: vmSettings.usunitto
        item.part = maxItem?.part ?: vmSettings.uspartto
        item.seqnum = (maxItem?.seqnum ?: 0) + 1
        return item
    }

    fun getNote(index: Int, onNext: () -> Unit) {
        val noteSite = noteSite ?: return
        val item = lstWords[index]
        val url = noteSite.url!!.replace("{0}", URLEncoder.encode(item.word, "UTF-8"))
        getHtml(url) {
            Log.d("", it)
            val result = extractTextFrom(it, noteSite.transformMac!!, noteSite.template!!) { _,_ -> "" }
            item.note = result
            updateNote(item.id, result) {
                onNext()
            }
        }
    }

    fun getNotes(ifEmpty: Boolean, onNext: (Int) -> Unit) {
        val noteSite = noteSite ?: return
        noteFromIndex = 0; noteToIndex = lstWords.size; noteIfEmpty = ifEmpty
        onNext(noteSite.wait!!)
    }

    fun getNextNote(onNextRow: (Int) -> Unit, onNext: () -> Unit) {
        if (noteIfEmpty)
            while (noteFromIndex < noteToIndex && !(lstWords[noteFromIndex].note ?: "").isEmpty())
                noteFromIndex++
        if (noteFromIndex >= noteToIndex)
            onNext()
        else {
            val i = noteFromIndex
            getNote(i) { onNextRow(i) }
            noteFromIndex++
        }
    }
}
