package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.services.wpp.LangPhraseService
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhrasesLangViewModel : LollyListViewModel(), KoinComponent {

    var lstPhrasesAll_ = MutableStateFlow(listOf<MLangPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableStateFlow(listOf<MLangPhrase>())
    var lstPhrases get() = lstPhrases_.value; set(v) { lstPhrases_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    private val noFilter get() = textFilter.isEmpty()

    private val langPhraseService by inject<LangPhraseService>()

    init {
        combine(lstPhrasesAll_, textFilter_, scopeFilterIndex_, ::Triple).onEach {
            lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.phrase else it.translation).contains(textFilter, true))
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getData() {
        isBusy = true
        lstPhrasesAll = langPhraseService.getDataByLang(vmSettings.selectedLang.id)
        isBusy = false
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
