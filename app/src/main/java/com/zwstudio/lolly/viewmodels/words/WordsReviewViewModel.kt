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
    val hasNext get() = index < count
    val currentItem get() = if (hasNext) lstWords[index] else null
    val currentWord get() = if (hasNext) lstWords[index].word else ""
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
    val checkEnabled = MutableLiveData(false)
    val wordTargetString = MutableLiveData("")
    val noteTargetString = MutableLiveData("")
    val wordHintString = MutableLiveData("")
    val wordTargetVisible = MutableLiveData(true)
    val noteTargetVisible = MutableLiveData(true)
    val wordHintVisible = MutableLiveData(true)
    val translationString = MutableLiveData("")
    val wordInputString = MutableLiveData("")
    val checkString = MutableLiveData("Check")
    val searchEnabled = MutableLiveData(false)

    fun newTest() = viewModelScope.launch {
        fun f() {
            lstCorrectIDs = mutableListOf()
            index = 0
            doTest()
            checkString.value = if (isTestMode) "Check" else "Next"
        }
        subscriptionTimer?.dispose()
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
                subscriptionTimer = Observable.interval(options.interval.toLong(), TimeUnit.SECONDS).applyIO().subscribe { check() }
        }
    }

    fun next() {
        index++
        if (isTestMode && !hasNext) {
            index = 0
            lstWords = lstWords.filter { !lstCorrectIDs.contains(it.id) }
        }
    }

    private suspend fun getTranslation(): String {
        val dictTranslation = vmSettings.selectedDictTranslation ?: return ""
        val url = dictTranslation.urlString(currentWord, vmSettings.lstAutoCorrect)
        return extractTextFrom(vmSettings.getHtml(url), dictTranslation.transform, "") { text, _ -> text }
    }

    fun check() = viewModelScope.launch {
        if (!isTestMode) {
            var b = true
            if (options.mode == ReviewMode.ReviewManual && wordInputString.value!!.isNotEmpty() && wordInputString.value != currentWord) {
                b = false
                incorrectVisible.value = true
            }
            if (b) {
                next()
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
            searchEnabled.value = true
            checkString.value = "Next"
            if (!hasNext) return@launch
            val o = currentItem!!
            val isCorrect = o.word == wordInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
            val o2 = wordFamiService.update(o.wordid, isCorrect)
            o.correct = o2.correct
            o.total = o2.total
            accuracyString.value = o.accuracy
        } else {
            next()
            doTest()
            checkString.value = "Check"
        }
    }

    private fun doTest() = viewModelScope.launch {
        indexVisible.value = hasNext
        correctVisible.value = false
        incorrectVisible.value = false
        accuracyVisible.value = isTestMode && hasNext
        checkEnabled.value = hasNext
        wordTargetString.value = currentWord
        noteTargetString.value = currentItem?.note ?: ""
        wordTargetVisible.value = !isTestMode
        noteTargetVisible.value = !isTestMode
        wordHintString.value = currentItem?.word?.length?.toString() ?: ""
        wordHintVisible.value = isTestMode
        translationString.value = ""
        wordInputString.value = ""
        searchEnabled.value = false
        doTestAction()
        if (hasNext) {
            indexString.value = "${index + 1}/$count"
            accuracyString.value = currentItem!!.accuracy
            translationString.value = getTranslation()
            if (translationString.value!!.isEmpty() && !options.speakingEnabled)
                wordInputString.value = currentWord
        } else if (options.mode == ReviewMode.ReviewAuto)
            subscriptionTimer?.dispose()
    }
}
