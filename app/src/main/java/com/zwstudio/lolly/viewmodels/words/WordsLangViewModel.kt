package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.views.vmSettings
import io.reactivex.rxjava3.core.Completable
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

    fun getData(): Completable =
        langWordService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstWordsAll = it; applyFilters(); Completable.complete() }

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

    fun getNote(item: MLangWord): Completable {
        return vmSettings.getNote(item.word).flatMapCompletable {
            item.note = it
            langWordService.updateNote(item.id, it)
        }
    }

    fun clearNote(item: MLangWord): Completable {
        item.note = SettingsViewModel.zeroNote
        return langWordService.updateNote(item.id, item.note)
    }
}
