package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WordsUnitBatchEditViewModel : ViewModel() {
    val unitChecked = MutableLiveData(false)
    val partChecked = MutableLiveData(false)
    val seqnumChecked = MutableLiveData(false)

    fun save() {
    }
}
