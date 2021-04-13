package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.ui.vmSettings
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.services.misc.HtmlService
import io.reactivex.rxjava3.core.Observable

interface IOnlineDict {
    fun getHtml(url: String): Observable<String>
    val getWord: String
    val getDict: MDictionary
    val getUrl: String
}

class SearchViewModel : ViewModel(), IOnlineDict {
    var word = ""

    val htmlService = HtmlService()

    override fun getHtml(url: String): Observable<String> =
        htmlService.getHtml(url)
    override val getWord: String get() = word
    override val getDict: MDictionary get() = vmSettings.selectedDictReference
    override val getUrl: String get() = getDict.urlString(word, vmSettings.lstAutoCorrect)
}
