package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MLangPhrase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PhrasesLangDetailViewModel(val item: MLangPhrase) : ViewModel() {
    val id = item.id
    val phrase = MutableStateFlow(item.phrase)
    val translation = MutableStateFlow(item.translation)
    val saveEnabled = MutableStateFlow(false)

    init {
        phrase.onEach {
            saveEnabled.value = it.isNotEmpty()
        }.launchIn(viewModelScope)
    }

    fun save() {
        item.phrase = vmSettings.autoCorrectInput(phrase.value)
        item.translation = translation.value
    }
}
