package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MUnitWord

class WordsUnitDetailViewModel(val item: MUnitWord) : ViewModel() {
    val id = MutableLiveData(item.id)
    val textbookname = MutableLiveData(item.textbookname)
    val unitIndex = MutableLiveData(item.unitIndex)
    val partIndex = MutableLiveData(item.partIndex)
    val seqnum = MutableLiveData(item.seqnum)
    val word = MutableLiveData(item.word)
    val note = MutableLiveData(item.note)
    val wordid = MutableLiveData(item.wordid)
    val famiid = MutableLiveData(item.famiid)
    val accuracy = MutableLiveData(item.accuracy)

    fun save() {
        item.unitIndex = unitIndex.value!!
        item.partIndex = partIndex.value!!
        item.seqnum = seqnum.value!!
        item.word = word.value!!
        item.note = note.value!!
    }
}
