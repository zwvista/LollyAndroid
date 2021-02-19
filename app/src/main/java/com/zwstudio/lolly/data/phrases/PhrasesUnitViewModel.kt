package com.zwstudio.lolly.data.phrases

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import com.zwstudio.lolly.service.wpp.LangPhraseService
import com.zwstudio.lolly.service.wpp.UnitPhraseService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
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

    lateinit var compositeDisposable: CompositeDisposable

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

    fun getDataInTextbook(): Observable<Unit> =
        unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
                vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .map { lstPhrasesAll = it; applyFilters() }
            .applyIO()

    fun getDataInLang(): Observable<Unit> =
        unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .map { lstPhrasesAll = it; applyFilters() }
            .applyIO()

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

    fun reindex(onNext: (Int) -> Unit) {
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
