package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitWord
import com.zwstudio.lolly.domain.ReviewMode
import com.zwstudio.lolly.service.HtmlService
import com.zwstudio.lolly.service.UnitWordService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsReviewModel : BaseViewModel2() {

    @Bean
    lateinit var unitWordService: UnitWordService
    @Bean
    lateinit var vmWordFami: WordsFamiViewModel
    @Bean
    lateinit var htmlService: HtmlService

    var lstWords = listOf<MUnitWord>()
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    var mode = ReviewMode.ReviewAuto
    val isTestMode: Boolean
        get() = mode == ReviewMode.Test

    fun newTest(shuffled: Boolean, levelge0only: Boolean): Observable<Unit> =
        unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map {
                lstWords = it
                lstCorrectIDs = mutableListOf()
                if (levelge0only) lstWords = lstWords.filter { it.level >= 0 }
                if (shuffled) lstWords = lstWords.shuffled()
                index = 0
            }
            .applyIO()

    val hasNext: Boolean
        get() = index < lstWords.size
    fun next() {
        index++
        if (isTestMode && !hasNext) {
            index = 0
            lstWords = lstWords.filter { !lstCorrectIDs.contains(it.id) }
        }
    }

    val currentItem: MUnitWord?
        get() = if (hasNext) lstWords[index] else null
    val currentWord: String
        get() = if (hasNext) lstWords[index].word else ""
    fun getTranslation(): Observable<String> {
        val mDictTranslation = vmSettings.selectedDictTranslation
        if (mDictTranslation == null) return Observable.empty()
        val url = mDictTranslation.urlString(currentWord, vmSettings.lstAutoCorrect)
        return htmlService.getHtml(url)
            .map {
                extractTextFrom(it, mDictTranslation.transform!!, "") { text, _ -> text }
            }
            .applyIO()
    }

    fun check(wordInput: String): Observable<Unit> {
        if (!hasNext) return Observable.empty()
        val o = currentItem!!
        val isCorrect = o.word == wordInput
        if (isCorrect) lstCorrectIDs.add(o.id)
        return vmWordFami.update(o.wordid, isCorrect).map {  }.applyIO()

    }
}
