package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.LangPhrase
import com.zwstudio.lolly.domain.LangPhrases
import com.zwstudio.lolly.restapi.RestLangPhrase
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel2() {

    var lstPhrases = mutableListOf<LangPhrase>()
    var isSwipeStarted = false

    fun getData(): Observable<LangPhrases> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .applyIO()

}
