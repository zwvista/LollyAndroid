package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel() {

    var lstPhrasesAll = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrases = MutableLiveData(listOf<MLangPhrase>())
    var isSwipeStarted = MutableLiveData(false)
    var isEditMode = MutableLiveData(false)
    var scopeFilter = MutableLiveData(SettingsViewModel.lstScopePhraseFilters[0].label)
    var textFilter = MutableLiveData("")
    val noFilter get() = textFilter.value!!.isEmpty()

    @Bean
    lateinit var langPhraseService: LangPhraseService

    fun applyFilters() {
        lstPhrases.value = if (noFilter) lstPhrasesAll.value!! else lstPhrasesAll.value!!.filter {
            (textFilter.value!!.isEmpty() || (if (scopeFilter.value!! == "Phrase") it.phrase else it.translation).contains(textFilter.value!!, true))
        }
    }

    fun getData() = viewModelScope.launch {
        lstPhrasesAll.value = langPhraseService.getDataByLang(vmSettings.selectedLang.id)
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
