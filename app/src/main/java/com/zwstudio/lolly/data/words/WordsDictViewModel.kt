package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.service.misc.HtmlService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsDictViewModel : BaseViewModel() {
    var lstWords = mutableListOf<String>()
    var selectedWordIndex = 0
    val selectedWord: String
        get() = lstWords[selectedWordIndex]

    @Bean
    lateinit var htmlService: HtmlService

    fun getHtml(url: String): Observable<String> =
        htmlService.getHtml(url)
}
