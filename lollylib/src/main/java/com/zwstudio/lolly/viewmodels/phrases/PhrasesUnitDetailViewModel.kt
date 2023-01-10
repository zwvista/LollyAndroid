package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import kotlinx.coroutines.flow.MutableStateFlow

class PhrasesUnitDetailViewModel(val item: MUnitPhrase) : ViewModel() {
    val id = item.id
    val textbookname = item.textbookname
    val unitIndex = MutableStateFlow(item.unitIndex)
    val partIndex = MutableStateFlow(item.partIndex)
    val seqnum = MutableStateFlow(item.seqnum.toString())
    val phrase = MutableStateFlow(item.phrase)
    val translation = MutableStateFlow(item.translation)
    val phraseid = item.phraseid

    fun save() {
        item.unitIndex = unitIndex.value
        item.partIndex = partIndex.value
        item.seqnum = seqnum.value.toInt()
        item.phrase = vmSettings.autoCorrectInput(phrase.value)
        item.translation = translation.value
    }
}
