package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.UnitPhrases
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class PhrasesUnitViewModel : BaseViewModel() {

    fun getData(onNext: (UnitPhrases) -> Unit) {
        app.retrofit.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vm.selectedTextbook.id}",
                "UNITPART,bt,${vm.usunitpartfrom},${vm.usunitpartto}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
