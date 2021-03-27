package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WordsUnitBatchEditViewModel : ViewModel() {
    val unitIsChecked = MutableLiveData(false)
    val partIsChecked = MutableLiveData(false)
    val seqnumIsChecked = MutableLiveData(false)

    fun save() {
    }
}
