package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.vmSettings
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsLangViewModel : DrawerListViewModel(), KoinComponent {

    private var lstWordsAll_ = MutableLiveData(listOf<MLangWord>())
    var lstWordsAll get() = lstWordsAll_.value!!; set(v) { lstWordsAll_.value = v }
    private var lstWords_ = MutableLiveData(listOf<MLangWord>())
    var lstWords get() = lstWords_.value!!; set(v) { lstWords_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
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
