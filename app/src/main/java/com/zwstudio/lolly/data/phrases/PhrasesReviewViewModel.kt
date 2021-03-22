package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.domain.misc.ReviewMode
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import com.zwstudio.lolly.service.wpp.UnitPhraseService
import kotlinx.coroutines.launch
import org.androidannotations.annotations.EBean

@EBean
class PhrasesReviewViewModel : BaseViewModel() {

    val unitPhraseService = UnitPhraseService()

    var lstPhrases = listOf<MUnitPhrase>()
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    var mode = ReviewMode.ReviewAuto
    val isTestMode: Boolean
        get() = mode == ReviewMode.Test

    fun newTest(shuffled: Boolean) = viewModelScope.launch {
        lstPhrases = unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        lstCorrectIDs = mutableListOf()
        if (shuffled) lstPhrases = lstPhrases.shuffled()
        index = 0
    }

    val hasNext: Boolean
        get() = index < lstPhrases.size
    fun next() {
        index++
        if (isTestMode && !hasNext) {
            index = 0
            lstPhrases = lstPhrases.filter { !lstCorrectIDs.contains(it.id) }
        }
    }

    val currentItem: MUnitPhrase?
        get() = if (hasNext) lstPhrases[index] else null
    val currentPhrase: String
        get() = if (hasNext) lstPhrases[index].phrase else ""

    fun check(phraseInput: String) {
        if (!hasNext) return
        val o = currentItem!!
        val isCorrect = o.phrase == phraseInput
        if (isCorrect) lstCorrectIDs.add(o.id)
    }

}