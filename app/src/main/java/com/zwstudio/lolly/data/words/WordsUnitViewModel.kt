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

    val lstWordsAll = MutableLiveData(listOf<MUnitWord>())
    val lstWords = MutableLiveData(listOf<MUnitWord>())
    val isSwipeStarted = MutableLiveData(false)
    val isEditMode = MutableLiveData(false)
    val scopeFilter = MutableLiveData(SettingsViewModel.lstScopeWordFilters[0].label)
    val textFilter = MutableLiveData("")
    val textbookFilter = MutableLiveData(0)
    val noFilter get() = textFilter.value!!.isEmpty() && textbookFilter.value!! == 0

    @Bean
    lateinit var unitWordService: UnitWordService

    fun applyFilters() {
        lstWords.value = if (noFilter) lstWordsAll.value!! else lstWordsAll.value!!.filter {
            (textFilter.value!!.isEmpty() || (if (scopeFilter.value!! == "Word") it.word else it.note).contains(textFilter.value!!, true)) &&
            (textbookFilter.value!! == 0 || it.textbookid == textbookFilter.value!!)
        }
    }

    fun getDataInTextbook() = viewModelScope.launch {
        lstWordsAll.value = unitWordService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        applyFilters()
    }

    fun getDataInLang() = viewModelScope.launch {
        lstWordsAll.value = unitWordService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
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
        for (i in 1..lstWords.value!!.size) {
            val item = lstWords.value!![i - 1]
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
        val maxItem = lstWords.value!!.maxWithOrNull(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
        textbook = vmSettings.selectedTextbook
    }

    fun getNote(index: Int) = viewModelScope.launch {
        val item = lstWords.value!![index]
        item.note = vmSettings.getNote(item.word)
        unitWordService.updateNote(item.id, item.note)
    }

    fun clearNote(index: Int) = viewModelScope.launch {
        val item = lstWords.value!![index]
        unitWordService.updateNote(item.id, SettingsViewModel.zeroNote)
    }

    suspend fun getNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmSettings.getNotes(lstWords.value!!.size, isNoteEmpty = {
            !ifEmpty || lstWords.value!![it].note.isEmpty()
        }, getOne = { i ->
            getNote(i)
            oneComplete(i)
        }, allComplete = allComplete)
    }

    suspend fun clearNotes(ifEmpty: Boolean, oneComplete: (Int) -> Unit, allComplete: () -> Unit) {
        vmSettings.clearNotes(lstWords.value!!.size, isNoteEmpty = {
            !ifEmpty || lstWords.value!![it].note.isEmpty()
        }, getOne = { i ->
            clearNote(i)
            oneComplete(i)
        }, allComplete = allComplete)
    }
}
