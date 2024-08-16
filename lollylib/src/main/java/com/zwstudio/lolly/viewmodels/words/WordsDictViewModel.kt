package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.IOnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.misc.MDictionary
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class WordsDictViewModel : ViewModel(), IOnlineDict, KoinComponent {
    var lstWords_ = MutableStateFlow(listOf<String>())
    var lstWords get() = lstWords_.value; set(v) { lstWords_.value = v }
    var selectedWordIndex_ = MutableStateFlow(0)
    var selectedWordIndex get() = selectedWordIndex_.value; set(v) { selectedWordIndex_.value = v }
    val selectedWord: String
        get() = lstWords[selectedWordIndex]

    override val getWord: String get() = selectedWord
    override val getDict: MDictionary get() = vmSettings.selectedDictReference

    fun next(delta: Int) {
        selectedWordIndex = (selectedWordIndex + delta + lstWords.size) % lstWords.size
    }
}
