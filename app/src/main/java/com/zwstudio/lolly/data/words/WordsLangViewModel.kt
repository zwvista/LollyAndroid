package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import com.zwstudio.lolly.service.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel() {

    var lstWordsAll = MutableLiveData(listOf<MLangWord>())
    var lstWords = MutableLiveData(listOf<MLangWord>())
    var isSwipeStarted = MutableLiveData(false)
    var isEditMode = MutableLiveData(false)
    var scopeFilter = MutableLiveData(SettingsViewModel.lstScopeWordFilters[0].label)
    var textFilter = MutableLiveData("")
    val noFilter get() = textFilter.value!!.isEmpty()

    @Bean
    lateinit var langWordService: LangWordService

    fun applyFilters() {
        lstWords.value = if (noFilter) lstWordsAll.value!! else lstWordsAll.value!!.filter {
            (textFilter.value!!.isEmpty() || (if (scopeFilter.value!! == "Word") it.word else it.note).contains(textFilter.value!!, true))
        }
    }

    fun getData() = viewModelScope.launch {
        lstWordsAll.value = langWordService.getDataByLang(vmSettings.selectedLang.id)
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
        val item = lstWords.value!![index]
        item.note = vmSettings.getNote(item.word)
        langWordService.updateNote(item.id, item.note)
    }
}
