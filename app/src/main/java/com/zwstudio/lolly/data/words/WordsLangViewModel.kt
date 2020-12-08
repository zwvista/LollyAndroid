package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.NoteViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import com.zwstudio.lolly.service.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel() {

    var lstWords = mutableListOf<MLangWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel

    @Bean
    lateinit var langWordService: LangWordService
    @Bean
    lateinit var wordFamiService: WordFamiService

    fun getData(): Observable<Unit> =
        langWordService.getDataByLang(vmSettings.selectedLang.id)
            .map { lstWords = it.toMutableList() }
            .applyIO()

    fun update(id: Int, langid: Int, word: String, note: String?): Observable<Unit> =
        langWordService.update(id, langid, word, note)
            .applyIO()

    fun create(langid: Int, word: String, note: String?): Observable<Int> =
        langWordService.create(langid, word, note)
            .applyIO()

    fun delete(item: MLangWord): Observable<Unit> =
        langWordService.delete(item)
            .applyIO()

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(index: Int): Observable<Unit> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).flatMap {
            item.note = it
            langWordService.updateNote(item.id, it)
        }
    }
}
