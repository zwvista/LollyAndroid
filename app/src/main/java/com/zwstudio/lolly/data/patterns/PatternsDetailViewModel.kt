package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MPattern

class PatternsDetailViewModel(item: MPattern): ViewModel() {
    val id = MutableLiveData(item.id)
    val pattern = MutableLiveData(item.pattern)
    val note = MutableLiveData(item.note)
    val tags = MutableLiveData(item.tags)

    fun save(item: MPattern) {
        item.id = id.value!!
        item.pattern = pattern.value!!
        item.note = note.value!!
        item.tags = tags.value!!
    }
}
