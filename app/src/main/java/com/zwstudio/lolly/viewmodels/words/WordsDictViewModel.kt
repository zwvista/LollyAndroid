package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.ui.vmSettings
import com.zwstudio.lolly.viewmodels.misc.IOnlineDict
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.services.misc.HtmlService
import io.reactivex.rxjava3.core.Observable

class WordsDictViewModel : ViewModel(), IOnlineDict {
    var lstWords = mutableListOf<String>()
    var selectedWordIndex_ = MutableLiveData(0)
    var selectedWordIndex get() = selectedWordIndex_.value!!; set(v) { selectedWordIndex_.value = v }
    val selectedWord: String
        get() = lstWords[selectedWordIndex]

    val htmlService = HtmlService()

    override fun getHtml(url: String): Observable<String> =
        htmlService.getHtml(url)
    override val getWord: String get() = selectedWord
    override val getDict: MDictionary get() = vmSettings.selectedDictReference
    override val getUrl: String get() = getDict.urlString(selectedWord, vmSettings.lstAutoCorrect)

    fun next(delta: Int) {
        selectedWordIndex = (selectedWordIndex + delta + lstWords.size) % lstWords.size
    }
}
