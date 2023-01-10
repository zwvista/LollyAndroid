package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MUnitWord
import kotlinx.coroutines.flow.MutableStateFlow

class WordsUnitDetailViewModel(val item: MUnitWord) : ViewModel() {
    val id = item.id
    val textbookname = item.textbookname
    val unitIndex = MutableStateFlow(item.unitIndex)
    val partIndex = MutableStateFlow(item.partIndex)
    val seqnum = MutableStateFlow(item.seqnum.toString())
    val word = MutableStateFlow(item.word)
    val note = MutableStateFlow(item.note)
    val wordid = item.wordid
    val famiid = item.famiid
    val accuracy = item.accuracy

    fun save() {
        item.unitIndex = unitIndex.value
        item.partIndex = partIndex.value
        item.seqnum = seqnum.value.toInt()
        item.word = word.value
        item.note = note.value
    }
}
