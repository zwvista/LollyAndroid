package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.LangWords
import com.zwstudio.lolly.restapi.RestLangWord
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel2() {

    fun getData(onNext: (LangWords) -> Unit) {
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
