package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.services.misc.HtmlService
import io.reactivex.rxjava3.core.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface IOnlineDict {
    fun getHtml(url: String): Single<String>
    val getWord: String
    val getDict: MDictionary
    val getUrl: String
}

class SearchViewModel : ViewModel(), IOnlineDict, KoinComponent {
    var word = ""

    private val htmlService by inject<HtmlService>()

    override fun getHtml(url: String): Single<String> = htmlService.getHtml(url)
    override val getWord: String get() = word
    override val getDict: MDictionary get() = vmSettings.selectedDictReference
    override val getUrl: String get() = getDict.urlString(word, vmSettings.lstAutoCorrect)
}
