package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.LangWords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel() {

    fun getData(onNext: (LangWords) -> Unit) {
        app.retrofit.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,${vm.selectedLang.id}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
