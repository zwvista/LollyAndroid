package com.zwstudio.lolly.data.phrases

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel() {

    var lstPhrases = mutableListOf<MLangPhrase>()
    var isSwipeStarted = false

    @Bean
    lateinit var langPhraseService: LangPhraseService

    fun getData(): Observable<Unit> =
        langPhraseService.getDataByLang(vmSettings.selectedLang.id)
            .map { lstPhrases = it.toMutableList() }
            .applyIO()

    fun update(id: Int, langid: Int, phrase: String, translation: String?): Observable<Unit> =
        langPhraseService.update(id, langid, phrase, translation)
            .applyIO()

    fun create(langid: Int, phrase: String, translation: String?): Observable<Int> =
        langPhraseService.create(langid, phrase, translation)
            .applyIO()

    fun delete(item: MLangPhrase): Observable<Unit> =
        langPhraseService.delete(item)
            .applyIO()

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}