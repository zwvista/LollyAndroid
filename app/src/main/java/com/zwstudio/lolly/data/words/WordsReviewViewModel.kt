package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.applyIO
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.extractTextFrom
import com.zwstudio.lolly.domain.misc.MReviewOptions
import com.zwstudio.lolly.domain.misc.ReviewMode
import com.zwstudio.lolly.domain.wpp.MUnitWord
import com.zwstudio.lolly.service.wpp.UnitWordService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit
import kotlin.math.min

class WordsReviewViewModel : ViewModel() {

    val unitWordService = UnitWordService()
    val vmWordFami = WordsFamiViewModel()

    lateinit var compositeDisposable: CompositeDisposable

    var lstWords = listOf<MUnitWord>()
    val count get() = lstWords.size
    var lstCorrectIDs = mutableListOf<Int>()
    var index = 0
    val hasNext get() = index < count
    val currentItem get() = if (hasNext) lstWords[index] else null
    val currentWord get() = if (hasNext) lstWords[index].word else ""
    val options = MReviewOptions()
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

    fun newTest() {
        fun f() {
            lstCorrectIDs = mutableListOf()
            index = 0
            doTest()
            checkString.value = if (isTestMode) "Check" else "Next"
        }
        subscriptionTimer?.dispose()
        if (options.mode == ReviewMode.Textbook)
            compositeDisposable.add(unitWordService.getDataByTextbook(vmSettings.selectedTextbook).applyIO().subscribe {
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
            compositeDisposable.add(unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook, vmSettings.usunitpartfrom, vmSettings.usunitpartto).applyIO().subscribe {
                lstWords = it
                val nFrom = count * (options.groupSelected - 1) / options.groupCount
                val nTo = count * options.groupSelected / options.groupCount
                lstWords = lstWords.subList(nFrom, nTo)
                if (options.shuffled) lstWords = lstWords.shuffled()
                f()
                if (options.mode == ReviewMode.ReviewAuto)
                    subscriptionTimer = Observable.interval(options.interval.toLong(), TimeUnit.SECONDS).applyIO().subscribe { check() }
            })
    }

    fun next() {
        index++
        if (isTestMode && !hasNext) {
            index = 0
            lstWords = lstWords.filter { !lstCorrectIDs.contains(it.id) }
        }
    }

    private fun getTranslation(): Observable<String> {
        val dictTranslation = vmSettings.selectedDictTranslation ?: return Observable.empty()
        val url = dictTranslation.urlString(currentWord, vmSettings.lstAutoCorrect)
        return vmSettings.getHtml(url)
            .map { extractTextFrom(it, dictTranslation.transform, "") { text, _ -> text } }
            .applyIO()
    }

    fun check() {
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
            if (!hasNext) return
            val o = currentItem!!
            val isCorrect = o.word == wordInputString.value
            if (isCorrect) lstCorrectIDs.add(o.id)
            compositeDisposable.add(vmWordFami.update(o.wordid, isCorrect).applyIO().subscribe {
                o.correct = it.correct
                o.total = it.total
                accuracyString.value = o.accuracy
            })
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
        if (hasNext) {
            indexString.value = "${index + 1}/$count"
            accuracyString.value = currentItem!!.accuracy
            compositeDisposable.add(getTranslation().subscribe {
                translationString.value = it
                if (it.isEmpty() && !options.speakingEnabled)
                    wordInputString.value = currentWord
            })
        } else if (options.mode == ReviewMode.ReviewAuto)
            subscriptionTimer?.dispose()
    }
}
