package com.zwstudio.lolly.viewmodels.words

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.mapButReplace
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.services.wpp.LangWordService
import com.zwstudio.lolly.services.wpp.UnitWordService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.viewmodels.misc.SettingsViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WordsUnitViewModel : DrawerListViewModel(), KoinComponent {

    val lstWordsAll_ = MutableStateFlow(listOf<MUnitWord>())
    var lstWordsAll get() = lstWordsAll_.value; set(v) { lstWordsAll_.value = v }
    val lstWords_ = MutableStateFlow(listOf<MUnitWord>())
    var lstWords get() = lstWords_.value; set(v) { lstWords_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    val textbookFilterIndex_ = MutableStateFlow(0)
    var textbookFilterIndex get() = textbookFilterIndex_.value; set(v) { textbookFilterIndex_.value = v }
    private val textbookFilter get() = vmSettings.lstTextbookFilters[textbookFilterIndex].value
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    private val unitWordService by inject<UnitWordService>()
    private val langWordService by inject<LangWordService>()

    init {
        combine(combine(lstWordsAll_, textbookFilterIndex_, textFilter_, ::Triple), scopeFilterIndex_, ::Pair).onEach {
            lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.word else it.note).contains(textFilter, true)) &&
                    (textbookFilter == 0 || it.textbookid == textbookFilter)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getDataInTextbook() {
        isBusy = true
        lstWordsAll = unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        isBusy = false
    }

    suspend fun getDataInLang() {
        isBusy = true
        lstWordsAll = unitWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
        isBusy = false
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
        val note = vmSettings.getNote(item.word)
        val newItem = item.copy(note = note).apply {
            textbook = item.textbook
        }
        lstWordsAll = lstWordsAll.mapButReplace(item, newItem)
        langWordService.updateNote(item.wordid, note)
    }

    suspend fun clearNote(item: MUnitWord) {
        item.note = SettingsViewModel.zeroNote
        langWordService.updateNote(item.wordid, item.note)
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) = viewModelScope.launch {
        isBusy = true
        vmSettings.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            viewModelScope.launch {
                getNote(lstWords[i])
                oneComplete(i)
            }
        }, allComplete = {
            viewModelScope.launch {
                isBusy = false
                allComplete()
            }
        })
    }

    fun clearNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) = viewModelScope.launch {
        isBusy = true
        vmSettings.clearNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            viewModelScope.launch {
                clearNote(lstWords[i])
                oneComplete(i)
            }
        }, allComplete = {
            viewModelScope.launch {
                isBusy = false
                allComplete()
            }
        })
    }
}
