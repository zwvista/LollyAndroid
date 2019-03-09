package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitPhrase
import com.zwstudio.lolly.restapi.RestUnitPhrase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.EBean

@EBean
class PhrasesTextbookViewModel : BaseViewModel2() {

    var lstPhrases = listOf<MUnitPhrase>()
    var isSwipeStarted = false

    lateinit var compositeDisposable: CompositeDisposable

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .map {
                lstPhrases = it.lst!!
                for (o in lstPhrases) {
                    val o2 = vmSettings.lstTextbooks.first { it.id == o.textbookid }
                    o.lstUnits = o2.lstUnits
                    o.lstParts = o2.lstParts
                }
            }
            .applyIO()
}
