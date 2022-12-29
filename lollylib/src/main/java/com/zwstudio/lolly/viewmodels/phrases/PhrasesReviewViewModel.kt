package com.zwstudio.lolly.viewmodels.phrases

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.services.wpp.UnitPhraseService
import com.zwstudio.lolly.vmSettings
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.min

class PhrasesReviewViewModel(private val doTestAction: PhrasesReviewViewModel.() -> Unit) : ViewModel() {

    val unitPhraseService = UnitPhraseService()

    var lstPhrases = listOf<MUnitPhrase>()
    val count get() = lstPhrases.size
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    val hasCurrent get() = lstPhrases.isNotEmpty() && (onRepeat.value == true || index in 0 until count)
    val currentItem get() = if (hasCurrent) lstPhrases[index] else null
    val currentPhrase get() = if (hasCurrent) lstPhrases[index].phrase else ""
    var options = MReviewOptions()
    val isTestMode get() = options.mode == ReviewMode.Test || options.mode == ReviewMode.Textbook
    var timer: Timer? = null

    val isSpeaking = MutableLiveData(true)
    val indexString = MutableLiveData("")
    val indexVisible = MutableLiveData(true)
    val correctVisible = MutableLiveData(false)
    val incorrectVisible = MutableLiveData(false)
    val checkNextEnabled = MutableLiveData(false)
    val checkNextString = MutableLiveData("Check")
    val checkPrevEnabled = MutableLiveData(false)
    val checkPrevString = MutableLiveData("Check")
    val checkPrevVisible = MutableLiveData(true)
    val phraseTargetString = MutableLiveData("")
    val phraseTargetVisible = MutableLiveData(true)
    val translationString = MutableLiveData("")
    val phraseInputString = MutableLiveData("")
    val onRepeat = MutableLiveData(true)
    val moveForward = MutableLiveData(true)
    val onRepeatVisible = MutableLiveData(true)
    val moveForwardVisible = MutableLiveData(true)

    fun newTest() = viewModelScope.launch {
        fun f() {
            lstCorrectIDs = mutableListOf()
            index = if (moveForward.value!!) 0 else count - 1
            doTest()
            checkNextString.value = if (isTestMode) "Check" else "Next"
            checkPrevString.value = if (isTestMode) "Check" else "Prev"
        }
        lstPhrases = listOf()
        lstCorrectIDs = mutableListOf()
        index = 0
        stopTimer()
        isSpeaking.value = options.speakingEnabled
        moveForward.value = options.moveForward
        moveForwardVisible.value = !isTestMode
        onRepeat.value = !isTestMode && options.onRepeat
        onRepeatVisible.value = !isTestMode
        checkPrevVisible.value = !isTestMode
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
                timer = Timer().apply {
                    val handler = Handler(Looper.getMainLooper())
                    schedule(0, options.interval.toLong() * 1000) {
                        handler.post {
                            check(true)
                        }
                    }
                }
        }
    }

    fun move(toNext: Boolean) {
        fun checkOnRepeat() {
            if (onRepeat.value!!) {
                index = (index + count) % count
            }
        }
        if (moveForward.value == toNext) {
            index++
            checkOnRepeat()
            if (isTestMode && !hasCurrent) {
                index = 0
                lstPhrases = lstPhrases.filter { !lstCorrectIDs.contains(it.id) }
            }
        } else {
            index--
            checkOnRepeat()
        }
    }

    fun check(toNext: Boolean) {
        if (!isTestMode) {
            var b = true
            if (options.mode == ReviewMode.ReviewManual && phraseInputString.value!!.isNotEmpty() && phraseInputString.value != currentPhrase) {
                b = false
                incorrectVisible.value = true
            }
            if (b) {
                move(toNext)
                doTest()
            }
        } else if (!correctVisible.value!! && !incorrectVisible.value!!) {
            phraseInputString.value = vmSettings.autoCorrectInput(phraseInputString.value!!)
            phraseTargetVisible.value = true
            if (phraseInputString.value == currentPhrase)
                correctVisible.value = true
            else
                incorrectVisible.value = true
            checkNextString.value = "Next"
            checkPrevString.value = "Prev"
            if (!hasCurrent) return
            val o = currentItem!!
            val isCorrect = o.phrase == phraseInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
        } else {
            move(toNext)
            doTest()
            checkNextString.value = "Check"
            checkPrevString.value = "Check"
        }
    }

    private fun doTest() {
        indexVisible.value = hasCurrent
        correctVisible.value = false
        incorrectVisible.value = false
        checkNextEnabled.value = hasCurrent
        checkPrevEnabled.value = hasCurrent
        phraseTargetString.value = currentPhrase
        translationString.value = currentItem?.translation ?: ""
        phraseTargetVisible.value = !isTestMode
        phraseInputString.value = ""
        doTestAction()
        if (hasCurrent)
            indexString.value = "${index + 1}/$count"
        else if (options.mode == ReviewMode.ReviewAuto)
            stopTimer()
    }

    fun stopTimer() {
        timer?.cancel()
    }
}
