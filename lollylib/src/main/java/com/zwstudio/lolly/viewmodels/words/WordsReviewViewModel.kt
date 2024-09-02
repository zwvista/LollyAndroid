package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.lib.R as libR
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode
import com.zwstudio.lolly.models.misc.extractTextFrom
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.services.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit
import kotlin.math.min

class WordsReviewViewModel(private val doTestAction: WordsReviewViewModel.() -> Unit) : ViewModel(), KoinComponent {

    private val unitWordService by inject<UnitWordService>()
    private val wordFamiService by inject<WordFamiService>()

    lateinit var compositeDisposable: CompositeDisposable

    var lstWords = listOf<MUnitWord>()
    val count get() = lstWords.size
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    val hasCurrent get() = lstWords.isNotEmpty() && (onRepeat.value || index in 0 until count)
    val currentItem get() = if (hasCurrent) lstWords[index] else null
    val currentWord get() = if (hasCurrent) lstWords[index].word else ""
    var options = MReviewOptions()
    val isTestMode get() = options.mode == ReviewMode.Test || options.mode == ReviewMode.Textbook
    var subscriptionTimer: Disposable? = null

    val isSpeaking = MutableStateFlow(true)
    val indexString = MutableStateFlow("")
    val indexVisible = MutableStateFlow(true)
    val correctVisible = MutableStateFlow(false)
    val incorrectVisible = MutableStateFlow(false)
    val accuracyString = MutableStateFlow("")
    val accuracyVisible = MutableStateFlow(true)
    val checkNextEnabled = MutableStateFlow(false)
    val checkNextStringRes = MutableStateFlow(libR.string.text_check)
    val checkPrevEnabled = MutableStateFlow(false)
    val checkPrevStringRes = MutableStateFlow(libR.string.text_check)
    val checkPrevVisible = MutableStateFlow(true)
    val wordTargetString = MutableStateFlow("")
    val noteTargetString = MutableStateFlow("")
    val wordHintString = MutableStateFlow("")
    val wordTargetVisible = MutableStateFlow(true)
    val noteTargetVisible = MutableStateFlow(true)
    val wordHintVisible = MutableStateFlow(true)
    val translationString = MutableStateFlow("")
    val wordInputString = MutableStateFlow("")
    val onRepeat = MutableStateFlow(true)
    val moveForward = MutableStateFlow(true)
    val onRepeatVisible = MutableStateFlow(true)
    val moveForwardVisible = MutableStateFlow(true)

    fun newTest() {
        fun f() {
            index = if (moveForward.value) 0 else count - 1
            doTest()
            checkNextStringRes.value = if (isTestMode) libR.string.text_check else libR.string.text_next
            checkPrevStringRes.value = if (isTestMode) libR.string.text_check else libR.string.text_prev
        }
        lstWords = listOf()
        lstCorrectIDs = mutableListOf()
        index = 0
        stopTimer()
        isSpeaking.value = options.speakingEnabled
        moveForward.value = options.moveForward
        moveForwardVisible.value = !isTestMode
        onRepeat.value = !isTestMode && options.onRepeat
        onRepeatVisible.value = !isTestMode
        checkPrevVisible.value = !isTestMode
        if (options.mode == ReviewMode.Textbook)
            compositeDisposable.add(unitWordService.getDataByTextbook(vmSettings.selectedTextbook).applyIO().subscribeBy {
                val lst2 = mutableListOf<MUnitWord>()
                for (o in it) {
                    val s = o.accuracy
                    val percentage = if (!s.endsWith("%")) 0.0 else s.trimEnd('%').toDouble()
                    val t = 6 - (percentage / 20.0).toInt()
                    for (i in 0 until t)
                        lst2.add(o)
                }
                val lst3 = mutableListOf<MUnitWord>()
                val cnt = min(options.reviewCount, it.size)
                while (lst3.size < cnt) {
                    val o = lst2.random()
                    if (!lst3.contains(o))
                        lst3.add(o)
                }
                lstWords = lst3.toList()
                f()
            })
        else
            compositeDisposable.add(unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto).applyIO().subscribeBy {
                lstWords = it
                val nFrom = count * (options.groupSelected - 1) / options.groupCount
                val nTo = count * options.groupSelected / options.groupCount
                lstWords = lstWords.subList(nFrom, nTo)
                if (options.shuffled) lstWords = lstWords.shuffled()
                f()
                if (options.mode == ReviewMode.ReviewAuto)
                    subscriptionTimer = Observable.interval(options.interval.toLong(), TimeUnit.SECONDS).applyIO().subscribe { check(true) }
            })
    }

    fun move(toNext: Boolean) {
        fun checkOnRepeat() {
            if (onRepeat.value) {
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

    private fun getTranslation(): Single<String> {
        val dictTranslation = vmSettings.selectedDictTranslation
        val url = dictTranslation.urlString(currentWord, vmSettings.lstAutoCorrect)
        return vmSettings.getHtml(url)
            .map { extractTextFrom(it, dictTranslation.transform, "") { text, _ -> text } }
            .applyIO()
    }

    fun check(toNext: Boolean) {
        if (!isTestMode) {
            var b = true
            if (options.mode == ReviewMode.ReviewManual && wordInputString.value.isNotEmpty() && wordInputString.value != currentWord) {
                b = false
                incorrectVisible.value = true
            }
            if (b) {
                move(toNext)
                doTest()
            }
        } else if (!correctVisible.value && !incorrectVisible.value) {
            wordInputString.value = vmSettings.autoCorrectInput(wordInputString.value)
            wordTargetVisible.value = true
            if (wordInputString.value == currentWord)
                correctVisible.value = true
            else
                incorrectVisible.value = true
            wordHintVisible.value = false
            checkNextStringRes.value = libR.string.text_next
            checkPrevStringRes.value = libR.string.text_prev
            if (!hasCurrent) return
            val o = currentItem!!
            val isCorrect = o.word == wordInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
            compositeDisposable.add(wordFamiService.update(o.wordid, isCorrect).applyIO().subscribeBy {
                o.correct = it.correct
                o.total = it.total
                accuracyString.value = o.accuracy
            })
        } else {
            move(toNext)
            doTest()
            checkNextStringRes.value = libR.string.text_check
            checkPrevStringRes.value = libR.string.text_check
        }
    }

    private fun doTest() {
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
            compositeDisposable.add(getTranslation().subscribeBy {
                translationString.value = it
                if (it.isEmpty() && !options.speakingEnabled)
                    wordInputString.value = currentWord
            })
        } else if (options.mode == ReviewMode.ReviewAuto)
            stopTimer()
    }

    fun stopTimer() {
        subscriptionTimer?.dispose()
    }
}
