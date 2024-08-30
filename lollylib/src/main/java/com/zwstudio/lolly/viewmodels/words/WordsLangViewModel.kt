package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsLangViewModel : DrawerListViewModel(), KoinComponent {

    var lstWordsAll_ = MutableStateFlow(listOf<MLangWord>())
    var lstWordsAll get() = lstWordsAll_.value; set(v) { lstWordsAll_.value = v }
    var lstWords_ = MutableStateFlow(listOf<MLangWord>())
    var lstWords get() = lstWords_.value; set(v) { lstWords_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    private val noFilter get() = textFilter.isEmpty()

    private val langWordService by inject<LangWordService>()

    init {
        combine(lstWordsAll_, textFilter_, scopeFilterIndex_, ::Triple).onEach {
            lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.word else it.note).contains(textFilter, true))
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getData() {
        isBusy = true
        lstWordsAll = langWordService.getDataByLang(vmSettings.selectedLang.id)
        isBusy = false
    }

    fun update(item: MLangWord) = viewModelScope.launch {
        langWordService.update(item)
    }

    fun create(item: MLangWord) = viewModelScope.launch {
        item.id = langWordService.create(item)
    }

    fun delete(item: MLangWord) = viewModelScope.launch {
        langWordService.delete(item)
    }

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(item: MLangWord) = viewModelScope.launch {
        item.note = vmSettings.getNote(item.word)
        langWordService.updateNote(item.id, item.note)
    }

    fun clearNote(item: MLangWord) = viewModelScope.launch {
        item.note = SettingsViewModel.zeroNote
        langWordService.updateNote(item.id, item.note)
    }
}
