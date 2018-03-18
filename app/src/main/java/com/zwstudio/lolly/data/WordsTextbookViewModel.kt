package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.TextbookWords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsTextbookViewModel : BaseViewModel() {

    fun getData(onNext: (TextbookWords) -> Unit) {
        app.retrofit.create(RestTextbookWord::class.java)
            .getDataByLang("LANGID,eq,${vm.selectedLang.id}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
