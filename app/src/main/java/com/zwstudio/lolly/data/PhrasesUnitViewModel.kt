package com.zwstudio.lolly.data

import android.util.Log
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

    fun updateSeqNum(id: Int, seqnum: Int, onNext: () -> Unit) {
        retrofit.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun update(id: Int, unit: Int, part: Int, seqnum: Int, phrase: String, translation: String, onNext: () -> Unit) {
        retrofit.create(RestUnitPhrase::class.java)
            .update(id, unit, part, seqnum, phrase, translation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun create(unit: Int, part: Int, seqnum: Int, phrase: String, translation: String, onNext: (Int) -> Unit) {
        retrofit.create(RestUnitPhrase::class.java)
            .create(unit, part, seqnum, phrase, translation)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext(it)
            }
    }

    fun delete(id: Int, onNext: () -> Unit) {
        retrofit.create(RestUnitPhrase::class.java)
            .delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

}
