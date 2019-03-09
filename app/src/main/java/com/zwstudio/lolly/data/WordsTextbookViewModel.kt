package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitWord
import com.zwstudio.lolly.restapi.RestUnitWord
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.EBean

@EBean
class WordsTextbookViewModel : BaseViewModel2() {

    var lstWords = listOf<MUnitWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel
    lateinit var compositeDisposable: CompositeDisposable

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .map {
                lstWords = it.lst!!
                for (o in lstWords) {
                    val o2 = vmSettings.lstTextbooks.first { it.id == o.textbookid }
                    o.lstUnits = o2.lstUnits
                    o.lstParts = o2.lstParts
                }
            }
            .applyIO()

    fun getNote(index: Int): Observable<Int> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).concatMap {
            item.note = it
            retrofitJson.create(WordsUnitViewModel::class.java).updateNote(item.id, it)
        }
    }

}
