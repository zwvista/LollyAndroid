package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import com.zwstudio.lolly.service.wpp.UnitPhraseService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesUnitViewModel : BaseViewModel() {

    var lstPhrasesAll_ = MutableLiveData(listOf<MUnitPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value!!; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableLiveData(listOf<MUnitPhrase>())
    var lstPhrases get() = lstPhrases_.value!!; set(v) { lstPhrases_.value = v }
    var isSwipeStarted_ = MutableLiveData(false)
    var isSwipeStarted get() = isSwipeStarted_.value!!; set(v) { isSwipeStarted_.value = v }
    var isEditMode_ = MutableLiveData(false)
    var isEditMode get() = isEditMode_.value!!; set(v) { isEditMode_.value = v }
    var scopeFilter_ = MutableLiveData(SettingsViewModel.lstScopePhraseFilters[0].label)
    var scopeFilter get() = scopeFilter_.value!!; set(v) { scopeFilter_.value = v }
    val textFilter_ = MutableLiveData("")
    var textFilter get() = textFilter_.value!!; set(v) { textFilter_.value = v }
    val textbookFilter_ = MutableLiveData(0)
    var textbookFilter get() = textbookFilter_.value!!; set(v) { textbookFilter_.value = v }
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    @Bean
    lateinit var unitPhraseService: UnitPhraseService

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Phrase") it.phrase else it.translation).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    fun getDataInTextbook() = viewModelScope.launch {
        lstPhrasesAll = unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        applyFilters()
    }

    fun getDataInLang() = viewModelScope.launch {
        lstPhrasesAll = unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
        applyFilters()
    }

    fun updateSeqNum(id: Int, seqnum: Int) = viewModelScope.launch {
        unitPhraseService.updateSeqNum(id, seqnum)
    }

    fun update(item: MUnitPhrase) = viewModelScope.launch {
        unitPhraseService.update(item)
    }

    fun create(item: MUnitPhrase) = viewModelScope.launch {
        item.id = unitPhraseService.create(item)
    }

    fun delete(item: MUnitPhrase) = viewModelScope.launch {
        unitPhraseService.delete(item)
    }

    fun reindex(onNext: (Int) -> Unit) = viewModelScope.launch {
        for (i in 1..lstPhrases.size) {
            val item = lstPhrases[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            updateSeqNum(item.id, i)
            onNext(i - 1)
        }
    }

    fun newUnitPhrase() = MUnitPhrase().apply {
        langid = vmSettings.selectedLang.id
        textbookid = vmSettings.ustextbook
        // https://stackoverflow.com/questions/33640864/how-to-sort-based-on-compare-multiple-values-in-kotlin
        val maxItem = lstPhrases.maxWithOrNull(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

}
