package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
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

    var lstPatternsAll = MutableLiveData(listOf<MPattern>())
    var lstPatterns = MutableLiveData(listOf<MPattern>())
    var isSwipeStarted = MutableLiveData(false)
    var isEditMode = MutableLiveData(false)
    var scopeFilter = MutableLiveData(SettingsViewModel.lstScopePatternFilters[0].label)
    var textFilter = MutableLiveData("")
    val noFilter get() = textFilter.value!!.isEmpty()

    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var patternService: PatternService

    fun applyFilters() {
        lstPatterns.value = if (noFilter) lstPatternsAll.value!! else lstPatternsAll.value!!.filter {
            (textFilter.value!!.isEmpty() || (if (scopeFilter.value!! == "Pattern") it.pattern else if (scopeFilter.value!! == "Note") it.note else it.tags).contains(textFilter.value!!, true))
        }
    }

    fun getData(): Observable<Unit> =
        patternService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .map { lstPatternsAll.value = it; applyFilters() }

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
