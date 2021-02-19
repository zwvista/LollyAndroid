package com.zwstudio.lolly.data.patterns

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.service.wpp.PatternService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsViewModel : BaseViewModel() {

    var lstPatternsAll = listOf<MPattern>()
    var lstPatterns = listOf<MPattern>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopePatternFilters[0].label
    var textFilter = ""
    val noFilter get() = textFilter.isEmpty()

    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var patternService: PatternService

    fun applyFilters() {
        lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Pattern") it.pattern else if (scopeFilter == "Note") it.note else it.tags).contains(textFilter, true))
        }
    }

    fun getData(): Observable<Unit> =
        patternService.getDataByLang(vmSettings.selectedLang.id)
            .map { lstPatternsAll = it; applyFilters() }
            .applyIO()

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
