package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.misc.MReviewOptions
import com.zwstudio.lolly.domain.misc.ReviewMode
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import com.zwstudio.lolly.service.wpp.UnitPhraseService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import org.androidannotations.annotations.EBean
import java.util.concurrent.TimeUnit
import kotlin.math.min

@EBean
class PhrasesReviewViewModel : BaseViewModel() {

    val unitPhraseService = UnitPhraseService()

    var lstPhrases = listOf<MUnitPhrase>()
    val count get() = lstPhrases.size
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    val hasNext get() = index < count
    val currentItem get() = if (hasNext) lstPhrases[index] else null
    val currentPhrase get() = if (hasNext) lstPhrases[index].phrase else ""
    val options = MReviewOptions()
    val isTestMode get() = options.mode == ReviewMode.Test || options.mode == ReviewMode.Textbook
    var subscriptionTimer: Disposable? = null

    val isSpeaking = MutableLiveData(true)
    val indexString = MutableLiveData("")
    val indexIsVisible = MutableLiveData(true)
    val correctIsVisible = MutableLiveData(false)
    val incorrectIsVisible = MutableLiveData(false)
    val checkEnabled = MutableLiveData(false)
    val phraseTargetString = MutableLiveData("")
    val phraseTargetIsVisible = MutableLiveData(true)
    val translationString = MutableLiveData("")
    val phraseInputString = MutableLiveData("")
    val checkString = MutableLiveData("Check")

    fun newTest() = viewModelScope.launch {
        fun f() {
            lstCorrectIDs = mutableListOf()
            index = 0
            doTest()
            checkString.value = if (isTestMode) "Check" else "Next"
        }
        subscriptionTimer?.dispose()
        if (options.mode == ReviewMode.Textbook) {
            val lst = unitPhraseService.getDataByTextbook(vmSettings.selectedTextbook)
            val cnt = min(options.reviewCount, lst.size)
            lstPhrases = lst.shuffled().subList(0, cnt)
            f()
        } else {
            lstPhrases = unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            val nFrom = count * (options.groupSelected - 1) / options.groupCount
            val nTo = count * options.groupSelected / options.groupCount
            lstPhrases = lstPhrases.subList(nFrom, nTo)
            if (options.shuffled) lstPhrases = lstPhrases.shuffled()
            f()
            if (options.mode == ReviewMode.ReviewAuto)
                subscriptionTimer = Observable.interval(options.interval.toLong(), TimeUnit.SECONDS).applyIO().subscribe { check() }
        }
    }

    fun next() {
        index++
        if (isTestMode && !hasNext) {
            index = 0
            lstPhrases = lstPhrases.filter { !lstCorrectIDs.contains(it.id) }
        }
    }

    fun check() {
        if (!isTestMode) {
            var b = true
            if (options.mode == ReviewMode.ReviewManual && phraseInputString.value!!.isNotEmpty() && phraseInputString.value != currentPhrase) {
                b = false
                incorrectIsVisible.value = true
            }
            if (b) {
                next()
                doTest()
            }
        } else if (!correctIsVisible.value!! && !incorrectIsVisible.value!!) {
            phraseInputString.value = vmSettings.autoCorrectInput(phraseInputString.value!!)
            phraseTargetIsVisible.value = true
            if (phraseInputString.value == currentPhrase)
                correctIsVisible.value = true
            else
                incorrectIsVisible.value = true
            checkString.value = "Next"
            if (!hasNext) return
            val o = currentItem!!
            val isCorrect = o.phrase == phraseInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
        } else {
            next()
            doTest()
            checkString.value = "Check"
        }
    }

    private fun doTest() {
        indexIsVisible.value = hasNext
        correctIsVisible.value = false
        incorrectIsVisible.value = false
        checkEnabled.value = hasNext
        phraseTargetString.value = currentPhrase
        translationString.value = currentItem?.translation ?: ""
        phraseTargetIsVisible.value = !isTestMode
        phraseInputString.value = ""
        if (hasNext)
            indexString.value = "${index + 1}/$count"
        else if (options.mode == ReviewMode.ReviewAuto)
            subscriptionTimer?.dispose()
    }
}
