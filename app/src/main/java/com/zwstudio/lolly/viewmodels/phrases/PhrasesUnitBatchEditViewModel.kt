package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhrasesUnitBatchEditViewModel : ViewModel() {
    val unitChecked = MutableLiveData(false)
    val partChecked = MutableLiveData(false)
    val seqnumChecked = MutableLiveData(false)

    fun save() {
    }
}
