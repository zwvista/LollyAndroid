package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.UnitPhrase
import com.zwstudio.lolly.restapi.RestLangPhrase
import com.zwstudio.lolly.restapi.RestUnitPhrase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.EBean
import java.net.URLEncoder

@EBean
class PhrasesUnitViewModel : BaseViewModel2() {

    var lstPhrases = mutableListOf<UnitPhrase>()
    var isSwipeStarted = false

    lateinit var compositeDisposable: CompositeDisposable

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${vmSettings.selectedTextbook.id}",
                "UNITPART,bt,${vmSettings.usunitpartfrom},${vmSettings.usunitpartto}")
            .map { lstPhrases = it.lst!!.toMutableList() }
            .applyIO()

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Int> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun update(id: Int, langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, langphraseid: Int, phrase: String, translation: String?): Observable<Int> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLangPhrase("LANGPHRASEID,eq,$langphraseid")
            .concatMap {
                val lstUnit = it.lst!!
                if (lstUnit.isEmpty())
                    Observable.empty<Int>()
                else
                    retrofitJson.create(RestLangPhrase::class.java)
                        .getDataById("ID,eq,$langphraseid")
                        .concatMap {
                            val lstLangOld = it.lst!!
                            if (!lstLangOld.isEmpty() && lstLangOld[0].phrase == phrase)
                                retrofitJson.create(RestLangPhrase::class.java).updateTranslation(langphraseid, translation)
                            else
                                retrofitJson.create(RestLangPhrase::class.java)
                                    .getDataByLangPhrase("LANGID,eq,$langid", "PHRASE,eq,${URLEncoder.encode(phrase, "UTF-8")}")
                                    .concatMap {
                                        val lstLangNew = it.lst!!
                                        fun f(): Observable<Int> {
                                            val itemLang = lstLangNew[0]
                                            val langphraseid = itemLang.id
                                            return if (itemLang.combinetranslation(translation))
                                                retrofitJson.create(RestLangPhrase::class.java).updateTranslation(langphraseid, itemLang.translation)
                                            else
                                                Observable.just(langphraseid)
                                        }
                                        if (lstUnit.size == 1)
                                            if (lstLangNew.isEmpty())
                                                retrofitJson.create(RestLangPhrase::class.java).update(langphraseid, langid, phrase, translation)
                                            else
                                                retrofitJson.create(RestLangPhrase::class.java).delete(langphraseid).concatMap { f() }
                                        else
                                            if (lstLangNew.isEmpty())
                                                retrofitJson.create(RestLangPhrase::class.java).create(langid, phrase, translation)
                                            else
                                                f()
                                    }
                        }
            }.concatMap {
                retrofitJson.create(RestUnitPhrase::class.java).update(id, textbookid, unit, part, seqnum, it)
                    .map { Log.d("", it.toString()) }
            }.applyIO()

    fun create(langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, langphraseid: Int, phrase: String, translation: String?): Observable<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLangPhrase("LANGID,eq,$langid", "PHRASE,eq,${URLEncoder.encode(phrase, "UTF-8")}")
            .concatMap {
                val lstLang = it.lst!!
                if (lstLang.isEmpty())
                    retrofitJson.create(RestLangPhrase::class.java).create(langid, phrase, translation)
                else {
                    val itemLang = lstLang[0]
                    val langphraseid = itemLang.id
                    val b = itemLang.combinetranslation(translation)
                    if (b)
                        retrofitJson.create(RestLangPhrase::class.java)
                            .updateTranslation(langphraseid, itemLang.translation)
                            .map { langphraseid }
                    else
                        Observable.just(langphraseid)
                }
            }.concatMap {
                retrofitJson.create(RestUnitPhrase::class.java).create(textbookid, unit, part, seqnum, it)
            }.applyIO()

    fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstPhrases.size) {
            val item = lstPhrases[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            compositeDisposable.add(updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            })
        }
    }

    fun newUnitPhrase() = UnitPhrase().apply {
        langid = vmSettings.selectedLang.id
        textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstPhrases.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

}
