package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitPhrase
import com.zwstudio.lolly.domain.ReviewMode
import com.zwstudio.lolly.service.UnitPhraseService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesReviewModel : BaseViewModel2() {

    @Bean
    lateinit var unitPhraseService: UnitPhraseService

    var lstPhrases = listOf<MUnitPhrase>()
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    var mode = ReviewMode.ReviewAuto
    val isTestMode: Boolean
        get() = mode == ReviewMode.Test

    fun newTest(shuffled: Boolean): Observable<Unit> =
        unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map {
                lstPhrases = it
                lstCorrectIDs = mutableListOf()
                if (shuffled) lstPhrases = lstPhrases.shuffled()
                index = 0
            }
            .applyIO()

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