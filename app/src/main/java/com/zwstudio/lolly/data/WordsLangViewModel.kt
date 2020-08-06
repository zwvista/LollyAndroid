package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MLangWord
import com.zwstudio.lolly.service.LangWordService
import com.zwstudio.lolly.service.WordFamiService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel2() {

    var lstWords = mutableListOf<MLangWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel

    @Bean
    lateinit var langWordService: LangWordService
    @Bean
    lateinit var wordFamiService: WordFamiService

    fun getData(): Observable<Unit> =
        langWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .map { lstWords = it.toMutableList() }
            .applyIO()

    fun update(id: Int, langid: Int, word: String, level: Int, note: String?): Observable<Int> =
        langWordService.update(id, langid, word, note)
            .applyIO()

    fun create(langid: Int, word: String, level: Int, note: String?): Observable<Int> =
        langWordService.create(langid, word, note)
            .applyIO()

    fun delete(item: MLangWord): Observable<Int> =
        langWordService.delete(item)
            .applyIO()

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(index: Int): Observable<Int> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).concatMap {
            item.note = it
            langWordService.updateNote(item.id, it)
        }
    }
}
