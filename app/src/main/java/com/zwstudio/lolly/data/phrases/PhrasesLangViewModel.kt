package com.zwstudio.lolly.data.phrases

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Phrase") it.phrase else it.translation).contains(textFilter, true))
        }
    }

    suspend fun getData() {
        val lst = langPhraseService.getDataByLang(vmSettings.selectedLang.id)
        withContext(Dispatchers.Main) { lstPhrasesAll = lst; applyFilters() }
    }

    suspend fun update(item: MLangPhrase) =
        langPhraseService.update(item)

    suspend fun create(item: MLangPhrase) {
        item.id = langPhraseService.create(item)
    }

    suspend fun delete(item: MLangPhrase) =
        langPhraseService.delete(item)

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}
