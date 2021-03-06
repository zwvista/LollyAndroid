package com.zwstudio.lolly.data.phrases

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import com.zwstudio.lolly.service.wpp.UnitPhraseService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PhrasesUnitViewModel : BaseViewModel() {

    var lstPhrasesAll = listOf<MUnitPhrase>()
    var lstPhrases = listOf<MUnitPhrase>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopePhraseFilters[0].label
    var textFilter = ""
    var textbookFilter = 0
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    @Bean
    lateinit var unitPhraseService: UnitPhraseService
    @Bean
    lateinit var langPhraseService: LangPhraseService

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Phrase") it.phrase else it.translation).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    suspend fun getDataInTextbook() {
        val lst = unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
            vmSettings.usunitpartfrom, vmSettings.usunitpartto)
        withContext(Dispatchers.Main) { lstPhrasesAll = lst; applyFilters() }
    }

    suspend fun getDataInLang() {
        val lst = unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
        withContext(Dispatchers.Main) { lstPhrasesAll = lst; applyFilters() }
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) =
        unitPhraseService.updateSeqNum(id, seqnum)

    suspend fun update(item: MUnitPhrase) =
        unitPhraseService.update(item)

    suspend fun create(item: MUnitPhrase) {
        item.id = unitPhraseService.create(item)
    }

    suspend fun delete(item: MUnitPhrase) =
        unitPhraseService.delete(item)

    suspend fun reindex(onNext: (Int) -> Unit) {
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
