package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.services.misc.HtmlService
import com.zwstudio.lolly.services.misc.vmSettings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface IOnlineDict {
    suspend fun getHtml(url: String): String
    val getWord: String
    val getDict: MDictionary
    val getUrl: String
}

class SearchViewModel : ViewModel(), IOnlineDict, KoinComponent {
    var word = ""

    private val htmlService by inject<HtmlService>()

    override suspend fun getHtml(url: String): String =
        htmlService.getHtml(url)
    override val getWord: String get() = word
    override val getDict: MDictionary get() = vmSettings.selectedDictReference
    override val getUrl: String get() = getDict.urlString(word, vmSettings.lstAutoCorrect)
}
