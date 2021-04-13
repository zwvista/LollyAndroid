package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhrasesUnitBatchEditViewModel : ViewModel() {
    val unitIsChecked = MutableLiveData(false)
    val partIsChecked = MutableLiveData(false)
    val seqnumIsChecked = MutableLiveData(false)

    fun save() {
    }
}
