package com.zwstudio.lolly.viewmodels.phrases

import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.services.wpp.UnitPhraseService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhrasesUnitViewModel : DrawerListViewModel(), KoinComponent {

    var lstPhrasesAll_ = MutableStateFlow(listOf<MUnitPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value; set(v) { lstPhrasesAll_.value = v }
    var lstPhrases_ = MutableStateFlow(listOf<MUnitPhrase>())
    var lstPhrases get() = lstPhrases_.value; set(v) { lstPhrases_.value = v }
    val scopeFilterIndex = MutableStateFlow(0)
    val textbookFilterIndex = MutableStateFlow(0)
    private val textbookFilter get() = vmSettings.lstTextbookFilters[textbookFilterIndex.value].value
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    lateinit var compositeDisposable: CompositeDisposable

    private val unitPhraseService by inject<UnitPhraseService>()

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.phrase else it.translation).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    fun getDataInTextbook(): Completable =
        unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
                vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .applyIO()
            .flatMapCompletable { lstPhrasesAll = it; applyFilters(); Completable.complete() }

    fun getDataInLang(): Completable =
        unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .applyIO()
            .flatMapCompletable { lstPhrasesAll = it; applyFilters(); Completable.complete() }

    fun updateSeqNum(id: Int, seqnum: Int): Completable =
        unitPhraseService.updateSeqNum(id, seqnum)
            .applyIO()

    fun update(item: MUnitPhrase): Completable =
        unitPhraseService.update(item)
            .applyIO()

    fun create(item: MUnitPhrase): Completable =
        unitPhraseService.create(item)
            .flatMapCompletable { item.id = it; Completable.complete() }
            .applyIO()

    fun delete(item: MUnitPhrase): Completable =
        unitPhraseService.delete(item)
            .applyIO()

    override fun reindex(onNext: (Int) -> Unit) {
        for (i in 1..lstPhrases.size) {
            val item = lstPhrases[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            compositeDisposable.add(updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            })
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
