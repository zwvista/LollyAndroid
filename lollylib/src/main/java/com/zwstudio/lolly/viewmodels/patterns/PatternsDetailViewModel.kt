package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MPattern
import kotlinx.coroutines.flow.MutableStateFlow

class PatternsDetailViewModel(val item: MPattern) : ViewModel() {
    val id = item.id
    val pattern = MutableStateFlow(item.pattern)
    val note = MutableStateFlow(item.note)
    val tags = MutableStateFlow(item.tags)

    fun save() {
        item.pattern = pattern.value
        item.note = note.value
        item.tags = tags.value
    }
}
