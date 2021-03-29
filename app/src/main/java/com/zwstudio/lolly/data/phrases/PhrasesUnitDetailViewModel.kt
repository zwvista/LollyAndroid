package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MUnitPhrase

class PhrasesUnitDetailViewModel(val item: MUnitPhrase) : ViewModel() {
    val id = MutableLiveData(item.id)
    val textbookname = MutableLiveData(item.textbookname)
    val unitItemPosition = MutableLiveData(item.unitItemPosition)
    val partItemPosition = MutableLiveData(item.partItemPosition)
    val seqnum = MutableLiveData(item.seqnum)
    val phrase = MutableLiveData(item.phrase)
    val translation = MutableLiveData(item.translation)
    val phraseid = MutableLiveData(item.phraseid)

    fun save() {
        item.unitItemPosition = unitItemPosition.value!!
        item.partItemPosition = partItemPosition.value!!
        item.seqnum = seqnum.value!!
        item.phrase = phrase.value!!
        item.translation = translation.value!!
    }
}
