package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MUnitWord
import com.zwstudio.lolly.service.wpp.UnitWordService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsUnitViewModel : BaseViewModel() {

    var lstWordsAll = listOf<MUnitWord>()
    var lstWords = listOf<MUnitWord>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopeWordFilters[0].label
    var textFilter = ""
    var textbookFilter = 0
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var unitWordService: UnitWordService

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Word") it.word else it.note).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    fun getDataInTextbook(): Observable<Unit> =
        unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map { lstWordsAll = it; applyFilters() }
            .applyIO()

    fun getDataInLang(): Observable<Unit> =
        unitWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .map { lstWordsAll = it; applyFilters() }
            .applyIO()

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Unit> =
        unitWordService.updateSeqNum(id, seqnum)
            .applyIO()

    fun updateNote(id: Int, note: String?): Observable<Unit> =
        unitWordService.updateNote(id, note)
            .applyIO()

    fun update(item: MUnitWord): Observable<Unit> =
        unitWordService.update(item)
            .applyIO()

    fun create(item: MUnitWord): Observable<Unit> =
        unitWordService.create(item)
            .map { item.id = it }
            .applyIO()

    fun delete(item: MUnitWord): Observable<Unit> =
        unitWordService.delete(item)
            .applyIO()

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
        textbookid = vmSettings.ustextbook
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstWords.maxWithOrNull(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
        textbook = vmSettings.selectedTextbook
    }

    fun getNote(index: Int): Observable<Unit> {
        val item = lstWords[index]
        return vmSettings.getNote(item.word).flatMap {
            item.note = it
            unitWordService.updateNote(item.id, it)
        }
    }

    fun clearNote(index: Int): Observable<Unit> {
        val item = lstWords[index]
        item.note = SettingsViewModel.zeroNote
        return vmSettings.getNote(item.word).flatMap {
            unitWordService.updateNote(item.id, it)
        }
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmSettings.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            compositeDisposable.add(getNote(i).subscribe { oneComplete(i) })
        }, allComplete = allComplete)
    }

    fun clearNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmSettings.clearNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            compositeDisposable.add(clearNote(i).subscribe { oneComplete(i) })
        }, allComplete = allComplete)
    }
}
