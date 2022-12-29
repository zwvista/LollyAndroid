package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.services.misc.applyIO
import com.zwstudio.lolly.services.misc.vmSettings
import com.zwstudio.lolly.services.wpp.LangPhraseService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import io.reactivex.rxjava3.core.Completable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhrasesLangViewModel : DrawerListViewModel(), KoinComponent {

    private var lstPhrasesAll_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value!!; set(v) { lstPhrasesAll_.value = v }
    private var lstPhrases_ = MutableLiveData(listOf<MLangPhrase>())
    var lstPhrases get() = lstPhrases_.value!!; set(v) { lstPhrases_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    private val noFilter get() = textFilter.isEmpty()

    private val langPhraseService by inject<LangPhraseService>()

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.phrase else it.translation).contains(textFilter, true))
        }
    }

    fun getData(): Completable =
        langPhraseService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstPhrasesAll = it; applyFilters(); Completable.complete() }

    fun update(item: MLangPhrase): Completable =
        langPhraseService.update(item)
            .applyIO()

    fun create(item: MLangPhrase): Completable =
        langPhraseService.create(item)
            .flatMapCompletable { item.id = it; Completable.complete() }
            .applyIO()

    fun delete(item: MLangPhrase): Completable =
        langPhraseService.delete(item)
            .applyIO()

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}
