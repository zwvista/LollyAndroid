package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.services.wpp.PatternService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PatternsViewModel : DrawerListViewModel(), KoinComponent {

    var lstPatternsAll_ = MutableStateFlow(listOf<MPattern>())
    var lstPatternsAll get() = lstPatternsAll_.value; set(v) { lstPatternsAll_.value = v }
    var lstPatterns_ = MutableStateFlow(listOf<MPattern>())
    var lstPatterns get() = lstPatterns_.value; set(v) { lstPatterns_.value = v }
    val scopeFilterIndex = MutableStateFlow(0)
    private val noFilter get() = textFilter.isEmpty()

    private val patternService by inject<PatternService>()

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
