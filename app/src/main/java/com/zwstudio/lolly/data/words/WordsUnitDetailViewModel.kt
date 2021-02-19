package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord

class WordsUnitDetailViewModel(item: MUnitWord): ViewModel() {
    val id = MutableLiveData(item.id)
    val textbookname = MutableLiveData(item.textbookname)
    val unitItemPosition = MutableLiveData(item.unitItemPosition)
    val partItemPosition = MutableLiveData(item.partItemPosition)
    val seqnum = MutableLiveData(item.seqnum)
    val word = MutableLiveData(item.word)
    val note = MutableLiveData(item.note)
    val wordid = MutableLiveData(item.wordid)
    val famiid = MutableLiveData(item.famiid)
    val accuracy = MutableLiveData(item.accuracy)

    fun save(item: MUnitWord) {
        item.unitItemPosition = unitItemPosition.value!!
        item.partItemPosition = partItemPosition.value!!
        item.seqnum = seqnum.value!!
        item.word = word.value!!
        item.note = note.value!!
    }
}
