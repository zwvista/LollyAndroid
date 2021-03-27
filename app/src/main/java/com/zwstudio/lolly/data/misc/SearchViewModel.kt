package com.zwstudio.lolly.data.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.service.misc.HtmlService
import org.androidannotations.annotations.EBean

interface IOnlineDict {
    suspend fun getHtml(url: String): String
    val getWord: String
    val getDict: MDictionary
    val getUrl: String
}

@EBean
class SearchViewModel : ViewModel(), IOnlineDict {
    var word = ""

    val htmlService = HtmlService()

    override suspend fun getHtml(url: String): String =
        htmlService.getHtml(url)
    override val getWord: String get() = word
    override val getDict: MDictionary get() = vmSettings.selectedDictReference
    override val getUrl: String get() = getDict.urlString(word, vmSettings.lstAutoCorrect)
}
