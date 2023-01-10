package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangWord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WordsLangDetailViewModel(val item: MLangWord) : ViewModel() {
    val id = item.id
    val word = MutableStateFlow(item.word)
    val note = MutableStateFlow(item.note)
    val famiid = item.famiid
    val accuracy = item.accuracy
    val saveEnabled = MutableStateFlow(false)

    init {
        word.onEach {
            saveEnabled.value = it.isNotEmpty()
        }.launchIn(viewModelScope)
    }

    fun save() {
        item.word = vmSettings.autoCorrectInput(word.value)
        item.note = note.value
    }
}
