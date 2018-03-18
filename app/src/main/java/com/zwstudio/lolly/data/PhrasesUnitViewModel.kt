package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.UnitPhrases
import com.zwstudio.lolly.restapi.RestUnitPhrase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class PhrasesUnitViewModel : BaseViewModel2() {

    fun getData(onNext: (UnitPhrases) -> Unit) {
        retrofit.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vm.selectedTextbook.id}",
                "UNITPART,bt,${vm.usunitpartfrom},${vm.usunitpartto}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

}
