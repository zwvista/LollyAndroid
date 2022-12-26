package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import com.zwstudio.lolly.views.vmSettings
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsUnitViewModel : DrawerListViewModel(), KoinComponent {

    private val lstWordsAll_ = MutableLiveData(listOf<MUnitWord>())
    var lstWordsAll get() = lstWordsAll_.value!!; set(v) { lstWordsAll_.value = v }
    private val lstWords_ = MutableLiveData(listOf<MUnitWord>())
    var lstWords get() = lstWords_.value!!; set(v) { lstWords_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    val textbookFilterIndex = MutableLiveData(0)
    private val textbookFilter get() = vmSettings.lstTextbookFilters[textbookFilterIndex.value!!].value
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    private val unitWordService by inject<UnitWordService>()
    private val langWordService by inject<LangWordService>()

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.word else it.note).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    suspend fun getDataInTextbook() {
        lstWordsAll = unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        applyFilters()
    }

    suspend fun getDataInLang() {
        lstWordsAll = unitWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
        applyFilters()
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) {
        unitWordService.updateSeqNum(id, seqnum)
    }

    fun update(item: MUnitWord) = viewModelScope.launch {
        unitWordService.update(item)
    }

    fun create(item: MUnitWord) = viewModelScope.launch {
        item.id = unitWordService.create(item)
    }

    fun delete(item: MUnitWord) = viewModelScope.launch {
        unitWordService.delete(item)
    }

    override fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstWords.size) {
            val item = lstWords[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            viewModelScope.launch {
                updateSeqNum(item.id, i)
                onNext(i - 1)
            }
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

    suspend fun getNote(item: MUnitWord) {
        item.note = vmSettings.getNote(item.word)
        langWordService.updateNote(item.id, item.note)
    }

    suspend fun clearNote(item: MUnitWord) {
        item.note = SettingsViewModel.zeroNote
        langWordService.updateNote(item.id, item.note)
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) = viewModelScope.launch {
        vmSettings.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            viewModelScope.launch {
                getNote(lstWords[i])
                oneComplete(i)
            }
        }, allComplete = allComplete)
    }

    fun clearNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) = viewModelScope.launch {
        vmSettings.clearNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            viewModelScope.launch {
                clearNote(lstWords[i])
                oneComplete(i)
            }
        }, allComplete = allComplete)
    }
}
