package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.views.vmSettings
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.services.wpp.LangPhraseService
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhrasesLangViewModel : DrawerListViewModel(), KoinComponent {

    private var lstPhrasesAll_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value!!; set(v) { lstPhrasesAll_.value = v }
    private var lstPhrases_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrases get() = lstPhrases_.value!!; set(v) { lstPhrases_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    private val noFilter get() = textFilter.isEmpty()

    private val langPhraseService by inject<LangPhraseService>()

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.phrase else it.translation).contains(textFilter, true))
        }
    }

    suspend fun getData() {
        lstPhrasesAll = langPhraseService.getDataByLang(vmSettings.selectedLang.id)
        applyFilters()
    }

    fun update(item: MLangPhrase) = viewModelScope.launch {
        langPhraseService.update(item)
    }

    fun create(item: MLangPhrase) = viewModelScope.launch {
        item.id = langPhraseService.create(item)
    }

    fun delete(item: MLangPhrase) = viewModelScope.launch {
        langPhraseService.delete(item)
    }

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}
