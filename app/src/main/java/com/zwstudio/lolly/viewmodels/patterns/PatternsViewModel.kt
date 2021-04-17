package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.views.vmSettings
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.services.wpp.PatternService
import kotlinx.coroutines.launch

class PatternsViewModel : DrawerListViewModel() {

    private var lstPatternsAll_ = MutableLiveData(listOf<MPattern>())
    var lstPatternsAll get() = lstPatternsAll_.value!!; set(v) { lstPatternsAll_.value = v }
    private var lstPatterns_ = MutableLiveData(listOf<MPattern>())
    var lstPatterns get() = lstPatterns_.value!!; set(v) { lstPatterns_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    private val noFilter get() = textFilter.isEmpty()

    private val patternService = PatternService()

    fun applyFilters() {
        lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.pattern else if (scopeFilterIndex.value == 1) it.note else it.tags).contains(textFilter, true))
        }
    }

    suspend fun getData() {
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
