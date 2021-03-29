package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord

class WordsLangDetailViewModel(val item: MLangWord) : ViewModel() {
    val id = MutableLiveData(item.id)
    val word = MutableLiveData(item.word)
    val note = MutableLiveData(item.note)
    val famiid = MutableLiveData(item.famiid)
    val accuracy = MutableLiveData(item.accuracy)

    fun save() {
        item.word = word.value!!
        item.note = note.value!!
    }
}
