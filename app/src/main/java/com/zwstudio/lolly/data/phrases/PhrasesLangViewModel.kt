package com.zwstudio.lolly.data.phrases

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel() {

    var lstPhrasesAll = listOf<MLangPhrase>()
    var lstPhrases = listOf<MLangPhrase>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopePhraseFilters[0].label
    var textFilter = ""
    val noFilter get() = textFilter.isEmpty()

    @Bean
    lateinit var langPhraseService: LangPhraseService

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Phrase") it.phrase else it.translation).contains(textFilter, true))
        }
    }

    fun getData(): Observable<Unit> =
        langPhraseService.getDataByLang(vmSettings.selectedLang.id)
            .map { lstPhrasesAll = it; applyFilters() }
            .applyIO()

    fun update(item: MLangPhrase): Observable<Unit> =
        langPhraseService.update(item)
            .applyIO()

    fun create(item: MLangPhrase): Observable<Int> =
        langPhraseService.create(item)
            .applyIO()

    fun delete(item: MLangPhrase): Observable<Unit> =
        langPhraseService.delete(item)
            .applyIO()

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}
