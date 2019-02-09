package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.LangPhrases
import com.zwstudio.lolly.restapi.RestLangPhrase
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel2() {

    fun getData(): Observable<LangPhrases> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .applyIO()

}
