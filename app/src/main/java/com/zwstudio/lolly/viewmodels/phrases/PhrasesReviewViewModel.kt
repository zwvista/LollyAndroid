package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.services.wpp.UnitPhraseService
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.views.vmSettings
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.min

class PhrasesReviewViewModel(private val doTestAction: PhrasesReviewViewModel.() -> Unit) : ViewModel() {

    val unitPhraseService = UnitPhraseService()

    var lstPhrases = listOf<MUnitPhrase>()
    val count get() = lstPhrases.size
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    val hasNext get() = index < count
    val currentItem get() = if (hasNext) lstPhrases[index] else null
    val currentPhrase get() = if (hasNext) lstPhrases[index].phrase else ""
    var options = MReviewOptions()
    val isTestMode get() = options.mode == ReviewMode.Test || options.mode == ReviewMode.Textbook
    var subscriptionTimer: Disposable? = null

    val isSpeaking = MutableLiveData(true)
    val indexString = MutableLiveData("")
    val indexVisible = MutableLiveData(true)
    val correctVisible = MutableLiveData(false)
    val incorrectVisible = MutableLiveData(false)
    val checkEnabled = MutableLiveData(false)
    val phraseTargetString = MutableLiveData("")
    val phraseTargetVisible = MutableLiveData(true)
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
                incorrectVisible.value = true
            }
            if (b) {
                next()
                doTest()
            }
        } else if (!correctVisible.value!! && !incorrectVisible.value!!) {
            phraseInputString.value = vmSettings.autoCorrectInput(phraseInputString.value!!)
            phraseTargetVisible.value = true
            if (phraseInputString.value == currentPhrase)
                correctVisible.value = true
            else
                incorrectVisible.value = true
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
        indexVisible.value = hasNext
        correctVisible.value = false
        incorrectVisible.value = false
        checkEnabled.value = hasNext
        phraseTargetString.value = currentPhrase
        translationString.value = currentItem?.translation ?: ""
        phraseTargetVisible.value = !isTestMode
        phraseInputString.value = ""
        doTestAction()
        if (hasNext)
            indexString.value = "${index + 1}/$count"
        else if (options.mode == ReviewMode.ReviewAuto)
            subscriptionTimer?.dispose()
    }
}
