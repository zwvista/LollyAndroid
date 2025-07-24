package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsLangViewModel : LollyListViewModel(), KoinComponent {

    var lstWordsAll_ = MutableStateFlow(listOf<MLangWord>())
    var lstWordsAll get() = lstWordsAll_.value; set(v) { lstWordsAll_.value = v }
    var lstWords_ = MutableStateFlow(listOf<MLangWord>())
    var lstWords get() = lstWords_.value; set(v) { lstWords_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    private val noFilter get() = textFilter.isEmpty()

    private val langWordService by inject<LangWordService>()

    init {
        combine(lstWordsAll_, textFilter_, scopeFilterIndex_, ::Triple).onEach {
            lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.word else it.note).contains(textFilter, true))
            }
        }.launchIn(viewModelScope)
    }

    fun getData(): Completable {
        isBusy = true
        return langWordService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstWordsAll = it; isBusy = false; Completable.complete() }
    }

    fun update(item: MLangWord): Completable =
        langWordService.update(item)
            .applyIO()

    fun create(item: MLangWord): Completable =
        langWordService.create(item)
            .flatMapCompletable { item.id = it; Completable.complete() }
            .applyIO()

    fun delete(item: MLangWord): Completable =
        langWordService.delete(item)
            .applyIO()

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(item: MLangWord): Completable =
        vmSettings.getNote(item.word).flatMapCompletable {
            item.note = it
            langWordService.updateNote(item.id, item.note)
        }

    fun clearNote(item: MLangWord): Completable {
        item.note = SettingsViewModel.zeroNote
        return langWordService.updateNote(item.id, item.note)
    }
}
