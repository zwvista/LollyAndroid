package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.services.wpp.PatternService
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PatternsViewModel : LollyListViewModel(), KoinComponent {

    var lstPatternsAll_ = MutableStateFlow(listOf<MPattern>())
    var lstPatternsAll get() = lstPatternsAll_.value; set(v) { lstPatternsAll_.value = v }
    var lstPatterns_ = MutableStateFlow(listOf<MPattern>())
    var lstPatterns get() = lstPatterns_.value; set(v) { lstPatterns_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    private val noFilter get() = textFilter.isEmpty()

    lateinit var compositeDisposable: CompositeDisposable

    private val patternService by inject<PatternService>()

    init {
        combine(lstPatternsAll_, textFilter_, scopeFilterIndex_, ::Triple).onEach {
            lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.pattern else it.tags).contains(textFilter, true))
            }
        }.launchIn(viewModelScope)
    }

    fun getData(): Completable {
        isBusy = true
        return patternService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstPatternsAll = it; isBusy = false; Completable.complete() }
    }

    fun update(item: MPattern): Completable =
        patternService.update(item)
            .applyIO()

    fun create(item: MPattern): Completable =
        patternService.create(item)
            .flatMapCompletable { item.id = it; Completable.complete() }
            .applyIO()

    fun delete(id: Int): Completable =
        patternService.delete(id)
            .applyIO()

    fun newPattern() = MPattern().apply {
        langid = vmSettings.selectedLang.id
    }

}
