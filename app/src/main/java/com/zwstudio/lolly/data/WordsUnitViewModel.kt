package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.UnitWord
import com.zwstudio.lolly.restapi.RestLangWord
import com.zwstudio.lolly.restapi.RestUnitWord
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.EBean
import java.net.URLEncoder

@EBean
class WordsUnitViewModel : BaseViewModel2() {

    var lstWords = mutableListOf<UnitWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel
    lateinit var compositeDisposable: CompositeDisposable

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .map { lstWords = it.lst!!.toMutableList() }
            .applyIO()

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun updateNote(id: Int, note: String): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .updateNote(id, note)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun update(id: Int, langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, wordid: Int, word: String, note: String?): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByLangWord("WORDID,eq,$wordid")
            .concatMap {
                val lstUnit = it.lst!!
                if (lstUnit.isEmpty())
                    Observable.empty<Int>()
                else
                    retrofitJson.create(RestLangWord::class.java)
                        .getDataById("ID,eq,$wordid")
                        .concatMap {
                            val lstLangOld = it.lst!!
                            if (!lstLangOld.isEmpty() && lstLangOld[0].word == word)
                                retrofitJson.create(RestLangWord::class.java).updateNote(wordid, note)
                            else
                                retrofitJson.create(RestLangWord::class.java)
                                    .getDataByLangWord("LANGID,eq,$langid", "WORD,eq,${URLEncoder.encode(word, "UTF-8")}")
                                    .concatMap {
                                        // Api is case insensitive
                                        val lstLangNew = it.lst!!.filter { it.word == word }
                                        fun f(): Observable<Int> {
                                            val itemLang = lstLangNew[0]
                                            val wordid = itemLang.id
                                            return if (itemLang.combineNote(note))
                                                retrofitJson.create(RestLangWord::class.java).updateNote(wordid, itemLang.note)
                                            else
                                                Observable.just(wordid)
                                        }
                                        if (lstUnit.size == 1)
                                            if (lstLangNew.isEmpty())
                                                retrofitJson.create(RestLangWord::class.java).update(wordid, langid, word, 0, note)
                                            else
                                                retrofitJson.create(RestLangWord::class.java).delete(wordid).concatMap { f() }
                                        else
                                            if (lstLangNew.isEmpty())
                                                retrofitJson.create(RestLangWord::class.java).create(langid, word, 0, note)
                                            else
                                                f()
                                    }
                        }
            }.concatMap {
                retrofitJson.create(RestUnitWord::class.java).update(id, textbookid, unit, part, seqnum, it)
                    .map { Log.d("", it.toString()) }
            }.applyIO()

    fun create(langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, wordid: Int, word: String, note: String?): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLangWord("LANGID,eq,$langid", "WORD,eq,${URLEncoder.encode(word, "UTF-8")}")
            .concatMap {
                // Api is case insensitive
                val lstLang = it.lst!!.filter { it.word == word }
                if (lstLang.isEmpty())
                    retrofitJson.create(RestLangWord::class.java).create(langid, word, 0, note)
                else {
                    val itemLang = lstLang[0]
                    val wordid = itemLang.id
                    val b = itemLang.combineNote(note)
                    if (b)
                        retrofitJson.create(RestLangWord::class.java)
                            .updateNote(wordid, itemLang.note)
                            .map { wordid }
                    else
                        Observable.just(wordid)
                }
            }.concatMap {
                retrofitJson.create(RestUnitWord::class.java).create(textbookid, unit, part, seqnum, it)
            }.applyIO()

    fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstWords.size) {
            val item = lstWords[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            compositeDisposable.add(updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            })
        }
    }

    fun newUnitWord() = UnitWord().apply {
        langid = vmSettings.selectedLang.id
        textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstWords.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

    fun getNote(index: Int): Observable<Int> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).concatMap {
            item.note = it
            retrofitJson.create(WordsUnitViewModel::class.java).updateNote(item.id, it)
        }
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmNote.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isNullOrEmpty()
        }, getOne = { i ->
            compositeDisposable.add(getNote(i).subscribe { oneComplete(i) })
        }, allComplete = allComplete)
    }
}
