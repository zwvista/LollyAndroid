package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
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

    fun getData(): Observable<Unit> =
        langPhraseService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .map { lstPhrasesAll.value = it; applyFilters() }

    fun update(item: MLangPhrase): Observable<Unit> =
        langPhraseService.update(item)
            .applyIO()

    fun create(item: MLangPhrase): Observable<Unit> =
        langPhraseService.create(item)
            .map { item.id = it }
            .applyIO()

    fun delete(item: MLangPhrase): Observable<Unit> =
        langPhraseService.delete(item)
            .applyIO()

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}
