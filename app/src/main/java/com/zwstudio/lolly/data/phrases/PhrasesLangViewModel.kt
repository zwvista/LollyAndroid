package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.android.applyIO
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import io.reactivex.rxjava3.core.Observable

class PhrasesLangViewModel : DrawerListViewModel() {

    var lstPhrasesAll_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value!!; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrases get() = lstPhrases_.value!!; set(v) { lstPhrases_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    val noFilter get() = textFilter.isEmpty()

    val langPhraseService = LangPhraseService()

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.phrase else it.translation).contains(textFilter, true))
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
