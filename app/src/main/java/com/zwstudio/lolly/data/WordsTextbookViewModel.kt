package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.TextbookWord
import com.zwstudio.lolly.restapi.RestTextbookWord
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.EBean

@EBean
class WordsTextbookViewModel : BaseViewModel2() {

    var lstWords = mutableListOf<TextbookWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel
    lateinit var compositeDisposable: CompositeDisposable

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestTextbookWord::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .map { lstWords = it.lst!!.toMutableList() }
            .applyIO()

    fun getNote(index: Int): Observable<Int> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).concatMap {
            item.note = it
            retrofitJson.create(WordsUnitViewModel::class.java).updateNote(item.id, it)
        }
    }

}
