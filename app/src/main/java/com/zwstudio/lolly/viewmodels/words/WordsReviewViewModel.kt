package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.views.vmSettings
import com.zwstudio.lolly.viewmodels.misc.extractTextFrom
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlin.math.min

class WordsReviewViewModel(private val doTestAction: WordsReviewViewModel.() -> Unit) : ViewModel() {

    val unitWordService = UnitWordService()
    val vmWordFami = WordsFamiViewModel()

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
    val indexIsVisible = MutableLiveData(true)
    val correctIsVisible = MutableLiveData(false)
    val incorrectIsVisible = MutableLiveData(false)
    val accuracyString = MutableLiveData("")
    val accuracyIsVisible = MutableLiveData(true)
    val checkEnabled = MutableLiveData(false)
    val wordTargetString = MutableLiveData("")
    val noteTargetString = MutableLiveData("")
    val wordHintString = MutableLiveData("")
    val wordTargetIsVisible = MutableLiveData(true)
    val noteTargetIsVisible = MutableLiveData(true)
    val wordHintIsVisible = MutableLiveData(true)
    val translationString = MutableLiveData("")
    val wordInputString = MutableLiveData("")
    val checkString = MutableLiveData("Check")
    val searchIsEnabled = MutableLiveData(false)

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
                incorrectIsVisible.value = true
            }
            if (b) {
                next()
                doTest()
            }
        } else if (!correctIsVisible.value!! && !incorrectIsVisible.value!!) {
            wordInputString.value = vmSettings.autoCorrectInput(wordInputString.value!!)
            wordTargetIsVisible.value = true
            if (wordInputString.value == currentWord)
                correctIsVisible.value = true
            else
                incorrectIsVisible.value = true
            wordHintIsVisible.value = false
            searchIsEnabled.value = true
            checkString.value = "Next"
            if (!hasNext) return@launch
            val o = currentItem!!
            val isCorrect = o.word == wordInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
            val o2 = vmWordFami.update(o.wordid, isCorrect)
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
        indexIsVisible.value = hasNext
        correctIsVisible.value = false
        incorrectIsVisible.value = false
        accuracyIsVisible.value = isTestMode && hasNext
        checkEnabled.value = hasNext
        wordTargetString.value = currentWord
        noteTargetString.value = currentItem?.note ?: ""
        wordTargetIsVisible.value = !isTestMode
        noteTargetIsVisible.value = !isTestMode
        wordHintString.value = currentItem?.word?.length?.toString() ?: ""
        wordHintIsVisible.value = isTestMode
        translationString.value = ""
        wordInputString.value = ""
        searchIsEnabled.value = false
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
