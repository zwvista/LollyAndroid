package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.TextbookWords
import com.zwstudio.lolly.restapi.RestTextbookWord
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsTextbookViewModel : BaseViewModel2() {

    fun getData(onNext: (TextbookWords) -> Unit) {
        retrofit.create(RestTextbookWord::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
