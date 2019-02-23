package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.TextbookPhrase
import com.zwstudio.lolly.restapi.RestTextbookPhrase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.EBean

@EBean
class PhrasesTextbookViewModel : BaseViewModel2() {

    var lstPhrases = mutableListOf<TextbookPhrase>()
    var isSwipeStarted = false

    lateinit var compositeDisposable: CompositeDisposable

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestTextbookPhrase::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .map {
                lstPhrases = it.lst!!.toMutableList()
                for (o in lstPhrases) {
                    o.lstUnits = unitsFrom(o.unitinfo)
                    o.lstParts = partsFrom(o.parts)
                }
            }
            .applyIO()
}
