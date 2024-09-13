package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.IOnlineDict
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class WordsDictViewModel(val lstWords: List<String>, index: Int) : ViewModel(), IOnlineDict, KoinComponent {
    var selectedWordIndex_ = MutableStateFlow(index)
    var selectedWordIndex get() = selectedWordIndex_.value; set(v) { selectedWordIndex_.value = v }
    val selectedWord: String
        get() = lstWords[selectedWordIndex]

    override val getWord: String get() = selectedWord

    fun next(delta: Int) {
        selectedWordIndex = (selectedWordIndex + delta + lstWords.size) % lstWords.size
    }
}
