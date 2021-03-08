package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MUnitWord
import com.zwstudio.lolly.service.wpp.UnitWordService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsUnitViewModel : BaseViewModel() {

    val lstWordsAll_ = MutableLiveData(listOf<MUnitWord>())
    var lstWordsAll get() = lstWordsAll_.value!!; set(v) { lstWordsAll_.value = v }
    val lstWords_ = MutableLiveData(listOf<MUnitWord>())
    var lstWords get() = lstWords_.value!!; set(v) { lstWords_.value = v }
    val isSwipeStarted_ = MutableLiveData(false)
    var isSwipeStarted get() = isSwipeStarted_.value!!; set(v) { isSwipeStarted_.value = v }
    val isEditMode_ = MutableLiveData(false)
    var isEditMode get() = isEditMode_.value!!; set(v) { isEditMode_.value = v }
    val scopeFilter_ = MutableLiveData(SettingsViewModel.lstScopeWordFilters[0].label)
    var scopeFilter get() = scopeFilter_.value!!; set(v) { scopeFilter_.value = v }
    val textFilter_ = MutableLiveData("")
    var textFilter get() = textFilter_.value!!; set(v) { textFilter_.value = v }
    val textbookFilter_ = MutableLiveData(0)
    var textbookFilter get() = textbookFilter_.value!!; set(v) { textbookFilter_.value = v }
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    val unitWordService = UnitWordService()

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Word") it.word else it.note).contains(textFilter, true)) &&
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

    fun updateSeqNum(id: Int, seqnum: Int) = viewModelScope.launch {
        unitWordService.updateSeqNum(id, seqnum)
    }

    fun updateNote(id: Int, note: String?) = viewModelScope.launch {
        unitWordService.updateNote(id, note)
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

    fun reindex(onNext: (Int) -> Unit) = viewModelScope.launch {
        for (i in 1..lstWords.size) {
            val item = lstWords[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            updateSeqNum(item.id, i)
            onNext(i - 1)
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

    fun getNote(index: Int) = viewModelScope.launch {
        val item = lstWords[index]
        item.note = vmSettings.getNote(item.word)
        unitWordService.updateNote(item.id, item.note)
    }

    fun clearNote(index: Int) = viewModelScope.launch {
        val item = lstWords[index]
        unitWordService.updateNote(item.id, SettingsViewModel.zeroNote)
    }

    fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) = viewModelScope.launch {
        vmSettings.getNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            getNote(i)
            oneComplete(i)
        }, allComplete = allComplete)
    }

    fun clearNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) = viewModelScope.launch {
        vmSettings.clearNotes(lstWords.size, isNoteEmpty = {
            !ifEmpty || lstWords[it].note.isEmpty()
        }, getOne = { i ->
            clearNote(i)
            oneComplete(i)
        }, allComplete = allComplete)
    }
}
