package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MUnitWord

class WordsUnitDetailViewModel(item: MUnitWord): ViewModel() {
    val id = MutableLiveData(item.id)
    val textbookname = MutableLiveData(item.textbookname)
    val unit = MutableLiveData(item.unit)
    val part = MutableLiveData(item.part)
    val seqnum = MutableLiveData(item.seqnum)
    val word = MutableLiveData(item.word)
    val note = MutableLiveData(item.note)
    val wordid = MutableLiveData(item.wordid)
    val famiid = MutableLiveData(item.famiid)
    val accuracy = MutableLiveData(item.accuracy)

    fun save(item: MUnitWord) {
        item.unit = unit.value!!
        item.part = part.value!!
        item.seqnum = seqnum.value!!
        item.word = word.value!!
        item.note = note.value!!
    }
}
