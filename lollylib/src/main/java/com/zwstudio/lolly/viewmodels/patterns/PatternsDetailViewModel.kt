package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MPattern

class PatternsDetailViewModel(val item: MPattern) : ViewModel() {
    val id = MutableLiveData(item.id)
    val pattern = MutableLiveData(item.pattern)
    val note = MutableLiveData(item.note)
    val tags = MutableLiveData(item.tags)

    fun save() {
        item.pattern = pattern.value!!
        item.note = note.value!!
        item.tags = tags.value!!
    }
}
