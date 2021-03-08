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

    var lstPhrasesAll_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value!!; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrases get() = lstPhrases_.value!!; set(v) { lstPhrases_.value = v }
    var isSwipeStarted_ = MutableLiveData(false)
    var isSwipeStarted get() = isSwipeStarted_.value!!; set(v) { isSwipeStarted_.value = v }
    var isEditMode_ = MutableLiveData(false)
    var isEditMode get() = isEditMode_.value!!; set(v) { isEditMode_.value = v }
    var scopeFilter_ = MutableLiveData(SettingsViewModel.lstScopePhraseFilters[0].label)
    var scopeFilter get() = scopeFilter_.value!!; set(v) { scopeFilter_.value = v }
    var textFilter_ = MutableLiveData("")
    var textFilter get() = textFilter_.value!!; set(v) { textFilter_.value = v }
    val noFilter get() = textFilter.isEmpty()

    val langPhraseService = LangPhraseService()

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Phrase") it.phrase else it.translation).contains(textFilter, true))
        }
    }

    fun getData(): Observable<Unit> =
        langPhraseService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .map { lstPhrasesAll = it; applyFilters() }

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
