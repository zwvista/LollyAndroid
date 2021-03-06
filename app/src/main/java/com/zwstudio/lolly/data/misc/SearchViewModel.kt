package com.zwstudio.lolly.data.misc

import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.service.misc.HtmlService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

interface IOnlineDict {
    suspend fun getHtml(url: String): String
    val getWord: String
    val getDict: MDictionary
    val getUrl: String
}

@EBean
class SearchViewModel : BaseViewModel(), IOnlineDict {
    var word = ""

    @Bean
    lateinit var htmlService: HtmlService

    override suspend fun getHtml(url: String): String =
        htmlService.getHtml(url)
    override val getWord: String get() = word
    override val getDict: MDictionary get() = vmSettings.selectedDictReference
    override val getUrl: String get() = getDict.urlString(word, vmSettings.lstAutoCorrect)
}
