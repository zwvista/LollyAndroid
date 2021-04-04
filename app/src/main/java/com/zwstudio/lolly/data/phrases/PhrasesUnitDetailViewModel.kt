package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase

class PhrasesUnitDetailViewModel(val item: MUnitPhrase) : ViewModel() {
    val id = MutableLiveData(item.id)
    val textbookname = MutableLiveData(item.textbookname)
    val unitIndex = MutableLiveData(item.unitIndex)
    val partIndex = MutableLiveData(item.partIndex)
    val seqnum = MutableLiveData(item.seqnum)
    val phrase = MutableLiveData(item.phrase)
    val translation = MutableLiveData(item.translation)
    val phraseid = MutableLiveData(item.phraseid)

    fun save() {
        item.unitIndex = unitIndex.value!!
        item.partIndex = partIndex.value!!
        item.seqnum = seqnum.value!!
        item.phrase = phrase.value!!
        item.translation = translation.value!!
    }
}
