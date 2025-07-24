package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.services.wpp.PatternService
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

    private val patternService by inject<PatternService>()

    init {
        combine(lstPatternsAll_, textFilter_, scopeFilterIndex_, ::Triple).onEach {
            lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.pattern else it.tags).contains(textFilter, true))
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getData() {
        isBusy = true
        lstPatternsAll = patternService.getDataByLang(vmSettings.selectedLang.id)
        isBusy = false
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
