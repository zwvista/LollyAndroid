package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WordsUnitBatchEditViewModel(val vm: WordsUnitViewModel) : ViewModel() {
    val unitChecked = MutableStateFlow(false)
    val partChecked = MutableStateFlow(false)
    val seqnumChecked = MutableStateFlow(false)
    val unitIndex = MutableStateFlow(vmSettings.lstUnits.indexOfFirst { it.value == vmSettings.usunitto })
    val partIndex = MutableStateFlow(vmSettings.lstParts.indexOfFirst { it.value == vmSettings.uspartto })
    val seqnum = MutableStateFlow("0")
    val saveEnabled = MutableStateFlow(false)

    init {
        combine(unitChecked, partChecked, seqnumChecked) { (t1, t2, t3) ->
            Triple(t1, t2, t3)
        }.onEach {
            saveEnabled.value = it.first || it.second || it.third;
        }.launchIn(viewModelScope)
    }

    fun save() {
        if (!(unitChecked.value || partChecked.value || seqnumChecked.value)) return
        for ((i, item) in vm.lstWords.withIndex()) {
            if (!item.isChecked) continue
            if (unitChecked.value) item.unit = vmSettings.lstUnits[unitIndex.value].value
            if (partChecked.value) item.part = vmSettings.lstParts[partIndex.value].value
            if (seqnumChecked.value) item.seqnum += seqnum.value.toInt()
            vm.update(item)
        }
    }
}
