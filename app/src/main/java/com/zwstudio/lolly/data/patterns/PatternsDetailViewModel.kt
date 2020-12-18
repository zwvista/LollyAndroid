package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MPattern
import org.androidannotations.annotations.EBean

@EBean
class PatternsDetailViewModel: ViewModel() {
    val id = MutableLiveData<Int>()
    val pattern = MutableLiveData<String>()
    val note = MutableLiveData<String>()
    val tags = MutableLiveData<String>()

    fun load(item: MPattern) {
        id.value = item.id
        pattern.value = item.pattern
        note.value = item.note
        tags.value = item.tags
    }
    fun save(item: MPattern) {
        item.id = id.value!!
        item.pattern = pattern.value!!
        item.note = note.value!!
        item.tags = tags.value!!
    }
}
