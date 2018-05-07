package com.zwstudio.lolly.data

import com.zwstudio.lolly.restapi.RestLangPhrase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel2() {

    fun getData() =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

}
