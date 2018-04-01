package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.UnitWords
import com.zwstudio.lolly.restapi.RestUnitWord
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.EBean

@EBean
class WordsUnitViewModel : BaseViewModel2() {

    fun getData(onNext: (UnitWords) -> Unit) {
        retrofit.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onNext)
    }

    fun updateSeqNum(id: Int, seqnum: Int, onNext: () -> Unit) {
        retrofit.create(RestUnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun update(id: Int, unit: Int, part: Int, seqnum: Int, word: String, note: String, onNext: () -> Unit) {
        retrofit.create(RestUnitWord::class.java)
            .update(id, unit, part, seqnum, word, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

    fun create(unit: Int, part: Int, seqnum: Int, word: String, note: String, onNext: (Int) -> Unit) {
        retrofit.create(RestUnitWord::class.java)
            .create(unit, part, seqnum, word, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext(it)
            }
    }

    fun delete(id: Int, onNext: () -> Unit) {
        retrofit.create(RestUnitWord::class.java)
            .delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("", it.toString())
                onNext()
            }
    }

}
