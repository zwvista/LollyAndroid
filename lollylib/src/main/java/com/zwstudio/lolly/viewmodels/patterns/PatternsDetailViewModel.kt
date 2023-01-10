package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MPattern
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PatternsDetailViewModel(val item: MPattern) : ViewModel() {
    val id = item.id
    val pattern = MutableStateFlow(item.pattern)
    val note = MutableStateFlow(item.note)
    val tags = MutableStateFlow(item.tags)
    val saveEnabled = MutableStateFlow(false)

    init {
        pattern.onEach {
            saveEnabled.value = it.isNotEmpty()
        }.launchIn(viewModelScope)
    }

    fun save() {
        item.pattern = vmSettings.autoCorrectInput(pattern.value)
        item.note = note.value
        item.tags = tags.value
    }
}
