package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MUnitWord
import kotlinx.coroutines.flow.MutableStateFlow

class WordsUnitDetailViewModel(val item: MUnitWord) : ViewModel() {
    val id = MutableStateFlow(item.id)
    val textbookname = MutableStateFlow(item.textbookname)
    val unitIndex = MutableStateFlow(item.unitIndex)
    val partIndex = MutableStateFlow(item.partIndex)
    val seqnum = MutableStateFlow(item.seqnum)
    val word = MutableStateFlow(item.word)
    val note = MutableStateFlow(item.note)
    val wordid = MutableStateFlow(item.wordid)
    val famiid = MutableStateFlow(item.famiid)
    val accuracy = MutableStateFlow(item.accuracy)

    fun save() {
        item.unitIndex = unitIndex.value
        item.partIndex = partIndex.value
        item.seqnum = seqnum.value
        item.word = word.value
        item.note = note.value
    }
}
