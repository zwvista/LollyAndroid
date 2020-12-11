package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import com.zwstudio.lolly.service.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel() {

    var lstWordsAll = listOf<MLangWord>()
    var lstWords = listOf<MLangWord>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopeWordFilters[0].label
    var textFilter = ""
    val noFilter get() = textFilter.isEmpty()

    @Bean
    lateinit var langWordService: LangWordService
    @Bean
    lateinit var wordFamiService: WordFamiService

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Word") it.word else it.note).contains(textFilter, true))
        }
    }

    fun getData(): Observable<Unit> =
        langWordService.getDataByLang(vmSettings.selectedLang.id)
            .map { lstWordsAll = it; applyFilters() }
            .applyIO()

    fun update(item: MLangWord): Observable<Unit> =
        langWordService.update(item)
            .applyIO()

    fun create(item: MLangWord): Observable<Int> =
        langWordService.create(item)
            .applyIO()

    fun delete(item: MLangWord): Observable<Unit> =
        langWordService.delete(item)
            .applyIO()

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(index: Int): Observable<Unit> {
        val item = lstWords[index]
        return vmSettings.getNote(item.word).flatMap {
            item.note = it
            langWordService.updateNote(item.id, it)
        }
    }
}
