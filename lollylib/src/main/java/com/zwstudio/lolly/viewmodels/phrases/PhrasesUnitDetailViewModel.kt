package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PhrasesUnitDetailViewModel(val item: MUnitPhrase) : ViewModel() {
    val id = item.id
    val textbookname = item.textbookname
    val unitIndex = MutableStateFlow(item.unitIndex)
    val partIndex = MutableStateFlow(item.partIndex)
    val seqnum = MutableStateFlow(item.seqnum.toString())
    val phrase = MutableStateFlow(item.phrase)
    val translation = MutableStateFlow(item.translation)
    val phraseid = item.phraseid
    val saveEnabled = MutableStateFlow(false)

    init {
        phrase.onEach {
            saveEnabled.value = it.isNotEmpty()
        }.launchIn(viewModelScope)
    }

    fun save() {
        item.unitIndex = unitIndex.value
        item.partIndex = partIndex.value
        item.seqnum = seqnum.value.toInt()
        item.phrase = vmSettings.autoCorrectInput(phrase.value)
        item.translation = translation.value
    }
}
