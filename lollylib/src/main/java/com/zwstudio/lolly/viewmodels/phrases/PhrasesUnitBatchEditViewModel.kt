package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PhrasesUnitBatchEditViewModel : ViewModel() {
    val unitChecked = MutableStateFlow(false)
    val partChecked = MutableStateFlow(false)
    val seqnumChecked = MutableStateFlow(false)

    fun save() {
    }
}
