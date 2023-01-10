package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MLangPhrase
import kotlinx.coroutines.flow.MutableStateFlow

class PhrasesLangDetailViewModel(val item: MLangPhrase) : ViewModel() {
    val id = item.id
    val phrase = MutableStateFlow(item.phrase)
    val translation = MutableStateFlow(item.translation)

    fun save() {
        item.phrase = phrase.value
        item.translation = translation.value
    }
}
