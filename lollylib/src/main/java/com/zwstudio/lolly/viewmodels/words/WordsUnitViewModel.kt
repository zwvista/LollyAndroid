package com.zwstudio.lolly.viewmodels.words

import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsUnitViewModel : DrawerListViewModel(), KoinComponent {

    val lstWordsAll_ = MutableStateFlow(listOf<MUnitWord>())
    var lstWordsAll get() = lstWordsAll_.value; set(v) { lstWordsAll_.value = v }
    val lstWords_ = MutableStateFlow(listOf<MUnitWord>())
    var lstWords get() = lstWords_.value; set(v) { lstWords_.value = v }
    val scopeFilterIndex = MutableStateFlow(0)
    val textbookFilterIndex = MutableStateFlow(0)
    private val textbookFilter get() = vmSettings.lstTextbookFilters[textbookFilterIndex.value].value
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    lateinit var compositeDisposable: CompositeDisposable

    private val unitWordService by inject<UnitWordService>()
    private val langWordService by inject<LangWordService>()

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.word else it.note).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    fun getDataInTextbook(): Completable =
        unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .applyIO()
            .flatMapCompletable { lstWordsAll = it; applyFilters(); Completable.complete() }

    fun getDataInLang(): Completable =
        unitWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .applyIO()
            .flatMapCompletable { lstWordsAll = it; applyFilters(); Completable.complete() }

    fun updateSeqNum(id: Int, seqnum: Int): Completable =
        unitWordService.updateSeqNum(id, seqnum)
            .applyIO()

    fun update(item: MUnitWord): Completable =
        unitWordService.update(item)
            .applyIO()

    fun create(item: MUnitWord): Completable =
        unitWordService.create(item)
            .flatMapCompletable { item.id = it; Completable.complete() }
            .applyIO()

    fun delete(item: MUnitWord): Completable =
        unitWordService.delete(item)
            .applyIO()

    override fun reindex(onNext: (Int) -> Unit) {
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

    fun getNote(item: MUnitWord): Completable =
        vmSettings.getNote(item.word).flatMapCompletable {
            item.note = it
            langWordService.updateNote(item.wordid, item.note)
        }

    fun clearNote(item: MUnitWord): Completable {
        item.note = SettingsViewModel.zeroNote
        return langWordService.updateNote(item.wordid, item.note)
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmSettings.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            compositeDisposable.add(getNote(lstWords[i]).subscribe { oneComplete(i) })
        }, allComplete = allComplete)
    }

    fun clearNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmSettings.clearNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            compositeDisposable.add(clearNote(lstWords[i]).subscribe { oneComplete(i) })
        }, allComplete = allComplete)
    }
}
