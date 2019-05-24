package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitPhrase
import com.zwstudio.lolly.domain.ReviewMode
import com.zwstudio.lolly.service.UnitPhraseService
import io.reactivex.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesReviewModel : BaseViewModel2() {

    @Bean
    lateinit var unitPhraseService: UnitPhraseService

    var lstPhrase = listOf<MUnitPhrase>()
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    var mode = ReviewMode.ReviewAuto
    val isTestMode: Boolean
        get() = mode == ReviewMode.Test

    fun newTest(mode: ReviewMode, shuffled: Boolean): Observable<Unit> {
        this.mode = mode
        return unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map {
                lstPhrase = it
                lstCorrectIDs = mutableListOf()
                if (shuffled) lstPhrase = lstPhrase.shuffled()
                index = 0
            }
    }

    val hasNext: Boolean
        get() = index < lstPhrase.size
    fun next() {
        index++
        if (isTestMode && !hasNext) {
            index = 0
            lstPhrase = lstPhrase.filter { !lstCorrectIDs.contains(it.id) }
        }
    }

    val currentItem: MUnitPhrase?
        get() = if (hasNext) lstPhrase[index] else null
    val currentPhrase: String
        get() = if (hasNext) lstPhrase[index].phrase else ""

    fun check(phraseInput: String) {
        if (!hasNext) return
        val o = currentItem!!
        val isCorrect = o.phrase == phraseInput
        if (isCorrect) lstCorrectIDs.add(o.id)
    }

}