package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.LangWords
import com.zwstudio.lolly.restapi.RestLangWord
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel2() {

    fun getData(): Observable<LangWords> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .applyIO()

}
