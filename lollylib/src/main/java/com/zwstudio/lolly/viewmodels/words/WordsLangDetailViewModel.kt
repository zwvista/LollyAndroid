package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MLangWord
import kotlinx.coroutines.flow.MutableStateFlow

class WordsLangDetailViewModel(val item: MLangWord) : ViewModel() {
    val id = item.id
    val word = MutableStateFlow(item.word)
    val note = MutableStateFlow(item.note)
    val famiid = item.famiid
    val accuracy = item.accuracy

    fun save() {
        item.word = word.value
        item.note = note.value
    }
}
