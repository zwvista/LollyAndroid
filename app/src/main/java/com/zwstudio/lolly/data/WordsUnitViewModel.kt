package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.MUnitWord
import com.zwstudio.lolly.service.LangWordService
import com.zwstudio.lolly.service.UnitWordService
import com.zwstudio.lolly.service.WordFamiService
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsUnitViewModel : BaseViewModel2() {

    var lstWords = listOf<MUnitWord>()
    var isSwipeStarted = false

    lateinit var vmNote: NoteViewModel
    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var unitWordService: UnitWordService;
    @Bean
    lateinit var langWordService: LangWordService;
    @Bean
    lateinit var wordFamiService: WordFamiService;

    fun getDataInTextbook(): Observable<Unit> =
        unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map { lstWords = it }
            .applyIO()

    fun getDataInLang(): Observable<Unit> =
        unitWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .map { lstWords = it }
            .applyIO()

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Int> =
        unitWordService.updateSeqNum(id, seqnum)
            .applyIO()

    fun updateNote(id: Int, note: String?): Observable<Int> =
        unitWordService.updateNote(id, note)
            .applyIO()

    fun update(id: Int, langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, wordid: Int, word: String, note: String?): Observable<Int> =
        unitWordService.getDataByLangWord(wordid)
            .concatMap {
                val lstUnit = it
                if (lstUnit.isEmpty())
                    Observable.empty<Int>()
                else
                    langWordService.getDataById(wordid)
                        .concatMap {
                            val lstLangOld = it
                            if (!lstLangOld.isEmpty() && lstLangOld[0].word == word)
                                langWordService.updateNote(wordid, note).map { wordid }
                            else
                                langWordService.getDataByLangWord(langid, word)
                                    .concatMap {
                                        val lstLangNew = it
                                        fun f(): Observable<Int> {
                                            val itemLang = lstLangNew[0]
                                            val wordid = itemLang.id
                                            return if (itemLang.combineNote(note))
                                                langWordService.updateNote(wordid, itemLang.note).map { wordid }
                                            else
                                                Observable.just(wordid)
                                        }
                                        if (lstUnit.size == 1)
                                            if (lstLangNew.isEmpty())
                                                langWordService.update(wordid, langid, word, note).map { wordid }
                                            else
                                                langWordService.delete(wordid).concatMap { f() }
                                        else
                                            if (lstLangNew.isEmpty())
                                                langWordService.create(langid, word, note)
                                            else
                                                f()
                                    }
                        }
            }.concatMap {
                unitWordService.update(id, textbookid, unit, part, seqnum, it)
            }.applyIO()

    fun create(langid: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int, wordid: Int, word: String, note: String?): Observable<Int> =
        langWordService.getDataByLangWord(langid, word)
            .concatMap {
                val lstLang = it
                if (lstLang.isEmpty())
                    langWordService.create(langid, word, note)
                else {
                    val itemLang = lstLang[0]
                    val wordid = itemLang.id
                    val b = itemLang.combineNote(note)
                    if (b)
                        langWordService.updateNote(wordid, itemLang.note).map { wordid }
                    else
                        Observable.just(wordid)
                }
            }.concatMap {
                unitWordService.create(textbookid, unit, part, seqnum, it)
            }.applyIO()

    fun delete(id: Int, wordid: Int, famiid: Int): Observable<Int> =
        unitWordService.delete(id)
            .concatMap {
                langWordService.delete(wordid)
            }.concatMap {
                wordFamiService.delete(famiid)
            }.applyIO()

    fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstWords.size) {
            val item = lstWords[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            compositeDisposable.add(updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            })
        }
    }

    fun newUnitWord() = MUnitWord().apply {
        langid = vmSettings.selectedLang.id
        textbookid = vmSettings.ustextbookid
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstWords.maxWith(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

    fun getNote(index: Int): Observable<Int> {
        val item = lstWords[index]
        return vmNote.getNote(item.word).concatMap {
            item.note = it
            unitWordService.updateNote(item.id, it)
        }
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmNote.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isNullOrEmpty()
        }, getOne = { i ->
            compositeDisposable.add(getNote(i).subscribe { oneComplete(i) })
        }, allComplete = allComplete)
    }
}
