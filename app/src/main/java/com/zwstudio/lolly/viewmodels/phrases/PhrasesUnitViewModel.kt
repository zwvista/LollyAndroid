package com.zwstudio.lolly.viewmodels.phrases

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.ui.applyIO
import com.zwstudio.lolly.ui.vmSettings
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.services.wpp.UnitPhraseService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PhrasesUnitViewModel : DrawerListViewModel() {

    private var lstPhrasesAll_ = MutableLiveData(listOf<MUnitPhrase>())
    var lstPhrasesAll get() = lstPhrasesAll_.value!!; set(v) { lstPhrasesAll_.value = v }
    private var lstPhrases_ = MutableLiveData(listOf<MUnitPhrase>())
    var lstPhrases get() = lstPhrases_.value!!; set(v) { lstPhrases_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    val textbookFilterIndex = MutableLiveData(0)
    private val textbookFilter get() = vmSettings.lstTextbookFilters[textbookFilterIndex.value!!].value
    val noFilter get() = textFilter.isEmpty() && textbookFilter == 0

    lateinit var compositeDisposable: CompositeDisposable

    private val unitPhraseService = UnitPhraseService()

    fun applyFilters() {
        lstPhrases = if (noFilter) lstPhrasesAll else lstPhrasesAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.phrase else it.translation).contains(textFilter, true)) &&
            (textbookFilter == 0 || it.textbookid == textbookFilter)
        }
    }

    fun getDataInTextbook(): Observable<Unit> =
        unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
                vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .applyIO()
            .map { lstPhrasesAll = it; applyFilters() }

    fun getDataInLang(): Observable<Unit> =
        unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .applyIO()
            .map { lstPhrasesAll = it; applyFilters() }

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Unit> =
        unitPhraseService.updateSeqNum(id, seqnum)
            .applyIO()

    fun update(item: MUnitPhrase): Observable<Unit> =
        unitPhraseService.update(item)
            .applyIO()

    fun create(item: MUnitPhrase): Observable<Unit> =
        unitPhraseService.create(item)
            .map { item.id = it }
            .applyIO()

    fun delete(item: MUnitPhrase): Observable<Unit> =
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
