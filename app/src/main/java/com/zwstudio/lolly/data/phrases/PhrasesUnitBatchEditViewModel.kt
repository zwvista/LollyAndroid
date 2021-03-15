package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase

class PhrasesUnitBatchEditViewModel(item: MUnitPhrase) : ViewModel() {
    val unitIsChecked = MutableLiveData(false)
    val partIsChecked = MutableLiveData(false)
    val seqnumIsChecked = MutableLiveData(false)

    fun save() {
    }
}
