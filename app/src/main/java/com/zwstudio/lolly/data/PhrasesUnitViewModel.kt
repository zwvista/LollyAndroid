package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitPhrase
import com.zwstudio.lolly.service.LangPhraseService
import com.zwstudio.lolly.service.UnitPhraseService
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesUnitViewModel : BaseViewModel2() {

    var lstPhrases = listOf<MUnitPhrase>()
    var isSwipeStarted = false

    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var unitPhraseService: UnitPhraseService;
    @Bean
    lateinit var langPhraseService: LangPhraseService;

    fun getDataInTextbook(): Observable<Unit> =
        unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
                vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map { lstPhrases = it }
            .applyIO()

    fun getDataInLang(): Observable<Unit> =
        unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .map { lstPhrases = it }
            .applyIO()

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Int> =
        unitPhraseService.updateSeqNum(id, seqnum)
            .applyIO()

    fun update(id: Int, langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, phraseid: Int, phrase: String, translation: String?): Observable<Int> =
        unitPhraseService.getDataByLangPhrase(phraseid)
            .concatMap {
                val lstUnit = it
                if (lstUnit.isEmpty())
                    Observable.empty<Int>()
                else
                    langPhraseService.getDataById(phraseid)
                        .concatMap {
                            val lstLangOld = it
                            if (!lstLangOld.isEmpty() && lstLangOld[0].phrase == phrase)
                                langPhraseService.updateTranslation(phraseid, translation).map { phraseid }
                            else
                                langPhraseService.getDataByLangPhrase(langid, phrase)
                                    .concatMap {
                                        val lstLangNew = it
                                        fun f(): Observable<Int> {
                                            val itemLang = lstLangNew[0]
                                            val phraseid = itemLang.id
                                            return if (itemLang.combinetranslation(translation))
                                                langPhraseService.updateTranslation(phraseid, itemLang.translation).map { phraseid }
                                            else
                                                Observable.just(phraseid)
                                        }
                                        if (lstUnit.size == 1)
                                            if (lstLangNew.isEmpty())
                                                langPhraseService.update(phraseid, langid, phrase, translation).map { phraseid }
                                            else
                                                langPhraseService.delete(phraseid).concatMap { f() }
                                        else
                                            if (lstLangNew.isEmpty())
                                                langPhraseService.create(langid, phrase, translation)
                                            else
                                                f()
                                    }
                        }
            }.concatMap {
                unitPhraseService.update(id, textbookid, unit, part, seqnum, it)
            }.applyIO()

    fun create(langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, phraseid: Int, phrase: String, translation: String?): Observable<Int> =
        langPhraseService.getDataByLangPhrase(langid, phrase)
            .concatMap {
                val lstLang = it
                if (lstLang.isEmpty())
                    langPhraseService.create(langid, phrase, translation)
                else {
                    val itemLang = lstLang[0]
                    val phraseid = itemLang.id
                    val b = itemLang.combinetranslation(translation)
                    if (b)
                        langPhraseService.updateTranslation(phraseid, itemLang.translation).map { phraseid }
                    else
                        Observable.just(phraseid)
                }
            }.concatMap {
                unitPhraseService.create(textbookid, unit, part, seqnum, it)
            }.applyIO()

    fun delete(id: Int, phraseid: Int): Observable<Int> =
        unitPhraseService.delete(id)
            .concatMap {
                unitPhraseService.getDataByLangPhrase(phraseid)
            }.concatMap {
                val lst = it
                if (!lst.isEmpty())
                    Observable.empty<Int>()
                else
                    langPhraseService.delete(phraseid)
            }.applyIO()

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

    fun newUnitPhrase() = MUnitPhrase().apply {
        langid = vmSettings.selectedLang.id
        textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstPhrases.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

}
