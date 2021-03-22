package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MLangPhrase

class PhrasesLangDetailViewModel(item: MLangPhrase) : ViewModel() {
    val id = MutableLiveData(item.id)
    val phrase = MutableLiveData(item.phrase)
    val translation = MutableLiveData(item.translation)

    fun save(item: MLangPhrase) {
        item.phrase = phrase.value!!
        item.translation = translation.value!!
    }
}
