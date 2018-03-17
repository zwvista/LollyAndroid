package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import com.zwstudio.lolly.domain.UnitWords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean

@EBean
class WordsUnitViewModel {

    @App
    lateinit var app: LollyApplication
    val vm: SettingsViewModel
       get() = app.vm

    fun getData(onNext: (UnitWords) -> Unit) {
        app.retrofit.create(RestUnitWord::class.java)
                .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vm.selectedTextbook.id}",
                        "UNITPART,bt,${vm.usunitpartfrom},${vm.usunitpartto}")
//                .getDataByTextbookUnitPart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext)
    }

}
