package com.zwstudio.lolly.data.words

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.data.misc.extractTextFrom
import com.zwstudio.lolly.domain.misc.ReviewMode
import com.zwstudio.lolly.domain.wpp.MUnitWord
import com.zwstudio.lolly.service.misc.HtmlService
import com.zwstudio.lolly.service.wpp.UnitWordService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsReviewModel : BaseViewModel() {

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

    fun newTest(shuffled: Boolean) = viewModelScope.launch {
        lstWords = unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        lstCorrectIDs = mutableListOf()
        if (shuffled) lstWords = lstWords.shuffled()
        index = 0
    }

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
    fun getTranslation(block: (String) -> Unit) = viewModelScope.launch {
        val mDictTranslation = vmSettings.selectedDictTranslation
        if (mDictTranslation == null)
            block("")
        else {
            val url = mDictTranslation.urlString(currentWord, vmSettings.lstAutoCorrect)
            val text = htmlService.getHtml(url)
            block(extractTextFrom(text, mDictTranslation.transform, "") { text, _ -> text })
        }
    }

    fun check(wordInput: String) = viewModelScope.launch {
        if (!hasNext) return@launch
        val o = currentItem!!
        val isCorrect = o.word == wordInput
        if (isCorrect) lstCorrectIDs.add(o.id)
        vmWordFami.update(o.wordid, isCorrect)
    }
}
