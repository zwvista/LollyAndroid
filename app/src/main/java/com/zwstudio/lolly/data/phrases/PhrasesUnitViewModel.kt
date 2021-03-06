package com.zwstudio.lolly.data.phrases

import androidx.lifecycle.MutableLiveData
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

    var lstPhrasesAll = MutableLiveData(listOf<MUnitPhrase>())
    var lstPhrases = MutableLiveData(listOf<MUnitPhrase>())
    var isSwipeStarted = MutableLiveData(false)
    var isEditMode = MutableLiveData(false)
    var scopeFilter = MutableLiveData(SettingsViewModel.lstScopePhraseFilters[0].label)
    var textFilter = MutableLiveData("")
    var textbookFilter = MutableLiveData(0)
    val noFilter get() = textFilter.value!!.isEmpty() && textbookFilter.value!! == 0

    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var unitPhraseService: UnitPhraseService

    fun applyFilters() {
        lstPhrases.value = if (noFilter) lstPhrasesAll.value!! else lstPhrasesAll.value!!.filter {
            (textFilter.value!!.isEmpty() || (if (scopeFilter.value!! == "Phrase") it.phrase else it.translation).contains(textFilter.value!!, true)) &&
            (textbookFilter.value!! == 0 || it.textbookid == textbookFilter.value!!)
        }
    }

    fun getDataInTextbook(): Observable<Unit> =
        unitPhraseService.getDataByTextbookUnitPart(vmSettings.selectedTextbook,
                vmSettings.usunitpartfrom, vmSettings.usunitpartto)
            .applyIO()
            .map { lstPhrasesAll.value = it; applyFilters() }

    fun getDataInLang(): Observable<Unit> =
        unitPhraseService.getDataByLang(vmSettings.selectedLang.id, vmSettings.lstTextbooks)
            .applyIO()
            .map { lstPhrasesAll.value = it; applyFilters() }

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
        for (i in 1..lstPhrases.value!!.size) {
            val item = lstPhrases.value!![i - 1]
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
        val maxItem = lstPhrases.value!!.maxWithOrNull(compareBy({ it.unit }, { it.part }, { it.seqnum }))
        unit = maxItem?.unit ?: vmSettings.usunitto
        part = maxItem?.part ?: vmSettings.uspartto
        seqnum = (maxItem?.seqnum ?: 0) + 1
    }

}
