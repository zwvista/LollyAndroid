package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord

class WordsUnitBatchEditViewModel(item: MUnitWord) : ViewModel() {
    val unitIsChecked = MutableLiveData(false)
    val partIsChecked = MutableLiveData(false)
    val seqnumIsChecked = MutableLiveData(false)

    fun save() {
    }
}
