package com.zwstudio.lolly.data.words

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.misc.IOnlineDict
import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.service.misc.HtmlService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordsDictViewModel : ViewModel(), IOnlineDict {
    var lstWords = mutableListOf<String>()
    var selectedWordIndex = 0
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
