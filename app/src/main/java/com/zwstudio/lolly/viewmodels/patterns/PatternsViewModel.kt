package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.views.vmSettings
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.services.wpp.PatternService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PatternsViewModel : DrawerListViewModel() {

    private var lstPatternsAll_ = MutableLiveData(listOf<MPattern>())
    var lstPatternsAll get() = lstPatternsAll_.value!!; set(v) { lstPatternsAll_.value = v }
    private var lstPatterns_ = MutableLiveData(listOf<MPattern>())
    var lstPatterns get() = lstPatterns_.value!!; set(v) { lstPatterns_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    private val noFilter get() = textFilter.isEmpty()

    lateinit var compositeDisposable: CompositeDisposable

    private val patternService = PatternService()

    fun applyFilters() {
        lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.pattern else if (scopeFilterIndex.value == 1) it.note else it.tags).contains(textFilter, true))
        }
    }

    fun getData(): Observable<Unit> =
        patternService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .map { lstPatternsAll = it; applyFilters() }

    fun update(item: MPattern): Observable<Unit> =
        patternService.update(item)
            .applyIO()

    fun create(item: MPattern): Observable<Unit> =
        patternService.create(item)
            .map { item.id = it }
            .applyIO()

    fun delete(id: Int): Observable<Unit> =
        patternService.delete(id)
            .applyIO()

    fun newPattern() = MPattern().apply {
        langid = vmSettings.selectedLang.id
    }

}
