package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.services.wpp.WordFamiService
import com.zwstudio.lolly.viewmodels.misc.extractTextFrom
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.views.vmSettings
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.math.min

class WordsReviewViewModel(private val doTestAction: WordsReviewViewModel.() -> Unit) : ViewModel(), KoinComponent {

    private val unitWordService by inject<UnitWordService>()
    private val wordFamiService by inject<WordFamiService>()

    var lstWords = listOf<MUnitWord>()
    val count get() = lstWords.size
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    val hasCurrent get() = lstWords.isNotEmpty() && (onRepeat.value == true || index in 0 until count)
    val currentItem get() = if (hasCurrent) lstWords[index] else null
    val currentWord get() = if (hasCurrent) lstWords[index].word else ""
    var options = MReviewOptions()
    val isTestMode get() = options.mode == ReviewMode.Test || options.mode == ReviewMode.Textbook
    var subscriptionTimer: Disposable? = null

    val isSpeaking = MutableLiveData(true)
    val indexString = MutableLiveData("")
    val indexVisible = MutableLiveData(true)
    val correctVisible = MutableLiveData(false)
    val incorrectVisible = MutableLiveData(false)
    val accuracyString = MutableLiveData("")
    val accuracyVisible = MutableLiveData(true)
    val checkNextEnabled = MutableLiveData(false)
    val checkNextString = MutableLiveData("Check")
    val checkPrevEnabled = MutableLiveData(false)
    val checkPrevString = MutableLiveData("Check")
    val checkPrevVisible = MutableLiveData(true)
    val wordTargetString = MutableLiveData("")
    val noteTargetString = MutableLiveData("")
    val wordHintString = MutableLiveData("")
    val wordTargetVisible = MutableLiveData(true)
    val noteTargetVisible = MutableLiveData(true)
    val wordHintVisible = MutableLiveData(true)
    val translationString = MutableLiveData("")
    val wordInputString = MutableLiveData("")
    val onRepeat = MutableLiveData(true)
    val moveForward = MutableLiveData(true)
    val onRepeatVisible = MutableLiveData(true)
    val moveForwardVisible = MutableLiveData(true)

    fun newTest() = viewModelScope.launch {
        fun f() {
            index = if (moveForward.value!!) 0 else count - 1
            doTest()
            checkNextString.value = if (isTestMode) "Check" else "Next"
            checkPrevString.value = if (isTestMode) "Check" else "Prev"
        }
        lstWords = listOf()
        lstCorrectIDs = mutableListOf()
        index = 0
        subscriptionTimer?.dispose()
        isSpeaking.value = options.speakingEnabled
        moveForward.value = options.moveForward
        moveForwardVisible.value = !isTestMode
        onRepeat.value = !isTestMode && options.onRepeat
        onRepeatVisible.value = !isTestMode
        checkPrevVisible.value = !isTestMode
        if (options.mode == ReviewMode.Textbook) {
            val lst = unitWordService.getDataByTextbook(vmSettings.selectedTextbook)
            val lst2 = mutableListOf<MUnitWord>()
            for (o in lst) {
                val s = o.accuracy
                val percentage = if (!s.endsWith("%")) 0.0 else s.trimEnd('%').toDouble()
                val t = 6 - (percentage / 20.0).toInt()
                for (i in 0 until t)
                    lst2.add(o)
            }
            val lst3 = mutableListOf<MUnitWord>()
            val cnt = min(options.reviewCount, lst.size)
            while (lst3.size < cnt) {
                val o = lst2.random()
                if (!lst3.contains(o))
                    lst3.add(o)
            }
            lstWords = lst3.toList()
            f()
        } else {
            lstWords = unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            val nFrom = count * (options.groupSelected - 1) / options.groupCount
            val nTo = count * options.groupSelected / options.groupCount
            lstWords = lstWords.subList(nFrom, nTo)
            if (options.shuffled) lstWords = lstWords.shuffled()
            f()
            if (options.mode == ReviewMode.ReviewAuto)
                subscriptionTimer = Observable.interval(options.interval.toLong(), TimeUnit.SECONDS).applyIO().subscribe { check(true) }
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
                lstWords = lstWords.filter { !lstCorrectIDs.contains(it.id) }
            }
        } else {
            index--
            checkOnRepeat()
        }
    }

    private suspend fun getTranslation(): String {
        val dictTranslation = vmSettings.selectedDictTranslation ?: return ""
        val url = dictTranslation.urlString(currentWord, vmSettings.lstAutoCorrect)
        return extractTextFrom(vmSettings.getHtml(url), dictTranslation.transform, "") { text, _ -> text }
    }

    fun check(toNext: Boolean) = viewModelScope.launch {
        if (!isTestMode) {
            var b = true
            if (options.mode == ReviewMode.ReviewManual && wordInputString.value!!.isNotEmpty() && wordInputString.value != currentWord) {
                b = false
                incorrectVisible.value = true
            }
            if (b) {
                move(toNext)
                doTest()
            }
        } else if (!correctVisible.value!! && !incorrectVisible.value!!) {
            wordInputString.value = vmSettings.autoCorrectInput(wordInputString.value!!)
            wordTargetVisible.value = true
            if (wordInputString.value == currentWord)
                correctVisible.value = true
            else
                incorrectVisible.value = true
            wordHintVisible.value = false
            checkNextString.value = "Next"
            checkPrevString.value = "Prev"
            if (!hasCurrent) return@launch
            val o = currentItem!!
            val isCorrect = o.word == wordInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
            val o2 = wordFamiService.update(o.wordid, isCorrect)
            o.correct = o2.correct
            o.total = o2.total
            accuracyString.value = o.accuracy
        } else {
            move(toNext)
            doTest()
            checkNextString.value = "Check"
            checkPrevString.value = "Check"
        }
    }

    private fun doTest() = viewModelScope.launch {
        indexVisible.value = hasCurrent
        correctVisible.value = false
        incorrectVisible.value = false
        accuracyVisible.value = isTestMode && hasCurrent
        checkNextEnabled.value = hasCurrent
        checkPrevEnabled.value = hasCurrent
        wordTargetString.value = currentWord
        noteTargetString.value = currentItem?.note ?: ""
        wordTargetVisible.value = !isTestMode
        noteTargetVisible.value = !isTestMode
        wordHintString.value = currentItem?.word?.length?.toString() ?: ""
        wordHintVisible.value = isTestMode
        translationString.value = ""
        wordInputString.value = ""
        doTestAction()
        if (hasCurrent) {
            indexString.value = "${index + 1}/$count"
            accuracyString.value = currentItem!!.accuracy
            translationString.value = getTranslation()
            if (translationString.value!!.isEmpty() && !options.speakingEnabled)
                wordInputString.value = currentWord
        } else if (options.mode == ReviewMode.ReviewAuto)
            subscriptionTimer?.dispose()
    }
}
