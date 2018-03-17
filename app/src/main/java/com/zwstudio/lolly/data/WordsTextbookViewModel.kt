package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import com.zwstudio.lolly.domain.TextbookWords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean

@EBean
class WordsTextbookViewModel {

    @App
    lateinit var app: LollyApplication
    val vm: SettingsViewModel
        get() = app.vm

    fun getData(onNext: (TextbookWords) -> Unit) {
        app.retrofit.create(RestTextbookWord::class.java)
                .getDataByLang("LANGID,eq,${vm.selectedLang.id}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext)
    }

}
