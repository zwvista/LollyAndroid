package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.services.wpp.LangPhraseService
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhrasesLangViewModel : LollyListViewModel(), KoinComponent {

    var lstPhrasesAll_ = MutableStateFlow(listOf<MLangPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableStateFlow(listOf<MLangPhrase>())
    var lstPhrases get() = lstPhrases_.value; set(v) { lstPhrases_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    private val noFilter get() = textFilter.isEmpty()

    private val langPhraseService by inject<LangPhraseService>()

    init {
        combine(lstPhrasesAll_, textFilter_, scopeFilterIndex_, ::Triple).onEach {
            lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.phrase else it.translation).contains(textFilter, true))
            }
        }.launchIn(viewModelScope)
    }

    fun getData(): Completable {
        isBusy = true
        return langPhraseService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstPhrasesAll = it; isBusy = false; Completable.complete() }
    }

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
