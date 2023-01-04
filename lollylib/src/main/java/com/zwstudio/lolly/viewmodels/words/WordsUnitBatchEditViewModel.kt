package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class WordsUnitBatchEditViewModel : ViewModel() {
    val unitChecked = MutableStateFlow(false)
    val partChecked = MutableStateFlow(false)
    val seqnumChecked = MutableStateFlow(false)

    fun save() {
    }
}
