package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.services.wpp.UnitPhraseService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhrasesUnitViewModel : DrawerListViewModel(), KoinComponent {

    var lstPhrasesAll_ = MutableStateFlow(listOf<MUnitPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableStateFlow(listOf<MUnitPhrase>())
    var lstPhrases get() = lstPhrases_.value; set(v) { lstPhrases_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    val scopeFilterIndex_ = MutableStateFlow(0)
    var scopeFilterIndex get() = scopeFilterIndex_.value; set(v) { scopeFilterIndex_.value = v }
    val textbookFilterIndex_ = MutableStateFlow(0)
    var textbookFilterIndex get() = textbookFilterIndex_.value; set(v) { textbookFilterIndex_.value = v }
    private val textbookFilter get() = vmSettings.lstTextbookFilters[textbookFilterIndex].value
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    private val unitPhraseService by inject<UnitPhraseService>()

    init {
        combine(combine(lstPhrasesAll_, textbookFilterIndex_, textFilter_, ::Triple), scopeFilterIndex_, ::Pair).onEach {
            lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
                (textFilter.isEmpty() || (if (scopeFilterIndex == 0) it.phrase else it.translation).contains(textFilter, true)) &&
                    (textbookFilter == 0 || it.textbookid == textbookFilter)
            }
        }.launchIn(viewModelScope)
    }

    suspend fun getDataInTextbook() {
        isBusy = true
        lstPhrasesAll = unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        isBusy = false
    }

    suspend fun getDataInLang() {
        isBusy = true
        lstPhrasesAll = unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
        isBusy = false
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) {
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

    override fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstPhrases.size) {
            val item = lstPhrases[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            viewModelScope.launch {
                updateSeqNum(item.id, i)
                onNext(i - 1)
            }
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
        textbook = vmSettings.selectedTextbook
    }

}
