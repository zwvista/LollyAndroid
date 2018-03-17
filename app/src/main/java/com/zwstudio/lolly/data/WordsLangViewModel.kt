package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import com.zwstudio.lolly.domain.LangWords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel {

    @App
    lateinit var app: LollyApplication
    val vm: SettingsViewModel
        get() = app.vm

    fun getData(onNext: (LangWords) -> Unit) {
        app.retrofit.create(RestLangWord::class.java)
                .getDataByLang("LANGID,eq,${vm.selectedLang.id}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext)
    }

}
