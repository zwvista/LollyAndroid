package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.LangPhrases
import com.zwstudio.lolly.restapi.RestLangPhrase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel2() {

    fun getData(onNext: (LangPhrases) -> Unit) {
        retrofit.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,${vm.selectedLang.id}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
