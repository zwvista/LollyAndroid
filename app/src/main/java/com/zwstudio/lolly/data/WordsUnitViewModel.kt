package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.UnitWords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsUnitViewModel : BaseViewModel() {

    fun getData(onNext: (UnitWords) -> Unit) {
        app.retrofit.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vm.selectedTextbook.id}",
                "UNITPART,bt,${vm.usunitpartfrom},${vm.usunitpartto}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
