package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.vmSettings
import kotlinx.coroutines.flow.MutableStateFlow

class PhrasesUnitBatchEditViewModel : ViewModel() {
    val unitChecked = MutableStateFlow(false)
    val partChecked = MutableStateFlow(false)
    val seqnumChecked = MutableStateFlow(false)
    val unitIndex = MutableStateFlow(vmSettings.lstUnits.indexOfFirst { it.value == vmSettings.usunitto })
    val partIndex = MutableStateFlow(vmSettings.lstParts.indexOfFirst { it.value == vmSettings.uspartto })
    val seqnum = MutableStateFlow("0")
    val saveEnabled = MutableStateFlow(false)

    fun save() {
    }
}
