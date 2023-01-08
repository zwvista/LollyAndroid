package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsLangViewModel : DrawerListViewModel(), KoinComponent {

    var lstWordsAll_ = MutableStateFlow(listOf<MLangWord>())
    var lstWordsAll get() = lstWordsAll_.value; set(v) { lstWordsAll_.value = v }
    var lstWords_ = MutableStateFlow(listOf<MLangWord>())
    var lstWords get() = lstWords_.value; set(v) { lstWords_.value = v }
    val scopeFilterIndex = MutableStateFlow(0)
    private val noFilter get() = textFilter.isEmpty()

    private val langWordService by inject<LangWordService>()

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.word else it.note).contains(textFilter, true))
        }
    }

    suspend fun getData() {
        lstWordsAll = langWordService.getDataByLang(vmSettings.selectedLang.id)
        applyFilters()
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
