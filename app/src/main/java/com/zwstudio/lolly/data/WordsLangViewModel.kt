package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.MLangWord
import com.zwstudio.lolly.restapi.RestLangWord
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel2() {

    var lstWords = mutableListOf<MLangWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}&order=WORD")
            .map { lstWords = it.lst!!.toMutableList() }
            .applyIO()

    fun update(id: Int, langid: Int, word: String, level: Int, note: String?): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .update(id, langid, word, level, note)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun create(langid: Int, word: String, level: Int, note: String?): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .create(langid, word, level, note)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(index: Int): Observable<Int> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).concatMap {
            item.note = it
            retrofitJson.create(WordsUnitViewModel::class.java).updateNote(item.id, it)
        }
    }
}
