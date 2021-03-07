package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.service.wpp.PatternService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsViewModel : BaseViewModel() {

    var lstPatternsAll_ = MutableLiveData(listOf<MPattern>())
    var lstPatternsAll get() = lstPatternsAll_.value!!; set(v) { lstPatternsAll_.value = v }
    var lstPatterns_ = MutableLiveData(listOf<MPattern>())
    var lstPatterns get() = lstPatterns_.value!!; set(v) { lstPatterns_.value = v }
    var isSwipeStarted_ = MutableLiveData(false)
    var isSwipeStarted get() = isSwipeStarted_.value!!; set(v) { isSwipeStarted_.value = v }
    var isEditMode_ = MutableLiveData(false)
    var isEditMode get() = isEditMode_.value!!; set(v) { isEditMode_.value = v }
    var scopeFilter_ = MutableLiveData(SettingsViewModel.lstScopePatternFilters[0].label)
    var scopeFilter get() = scopeFilter_.value!!; set(v) { scopeFilter_.value = v }
    var textFilter_ = MutableLiveData("")
    var textFilter get() = textFilter_.value!!; set(v) { textFilter_.value = v }
    val noFilter get() = textFilter.isEmpty()

    @Bean
    lateinit var patternService: PatternService

    fun applyFilters() {
        lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Pattern") it.pattern else if (scopeFilter == "Note") it.note else it.tags).contains(textFilter, true))
        }
    }

    fun getData() = viewModelScope.launch {
        lstPatternsAll = patternService.getDataByLang(vmSettings.selectedLang.id)
        applyFilters()
    }

    fun update(item: MPattern) = viewModelScope.launch {
        patternService.update(item)
    }

    fun create(item: MPattern) = viewModelScope.launch {
        item.id = patternService.create(item)
    }

    fun delete(id: Int) = viewModelScope.launch {
        patternService.delete(id)
    }

    fun newPattern() = MPattern().apply {
        langid = vmSettings.selectedLang.id
    }

}
