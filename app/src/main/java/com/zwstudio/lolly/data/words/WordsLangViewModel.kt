package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import kotlinx.coroutines.launch
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : DrawerListViewModel() {

    var lstWordsAll_ = MutableLiveData(listOf<MLangWord>())
    var lstWordsAll get() = lstWordsAll_.value!!; set(v) { lstWordsAll_.value = v }
    var lstWords_ = MutableLiveData(listOf<MLangWord>())
    var lstWords get() = lstWords_.value!!; set(v) { lstWords_.value = v }
    var scopeFilter_ = MutableLiveData(SettingsViewModel.lstScopeWordFilters[0].label)
    var scopeFilter get() = scopeFilter_.value!!; set(v) { scopeFilter_.value = v }
    val noFilter get() = textFilter.isEmpty()

    val langWordService = LangWordService()

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Word") it.word else it.note).contains(textFilter, true))
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

    fun getNote(index: Int) = viewModelScope.launch {
        val item = lstWords[index]
        item.note = vmSettings.getNote(item.word)
        langWordService.updateNote(item.id, item.note)
    }
}
