package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.android.applyIO
import com.zwstudio.lolly.android.vmSettings
import com.zwstudio.lolly.data.DrawerListViewModel
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import io.reactivex.rxjava3.core.Observable

class WordsLangViewModel : DrawerListViewModel() {

    private var lstWordsAll_ = MutableLiveData(listOf<MLangWord>())
    var lstWordsAll get() = lstWordsAll_.value!!; set(v) { lstWordsAll_.value = v }
    private var lstWords_ = MutableLiveData(listOf<MLangWord>())
    var lstWords get() = lstWords_.value!!; set(v) { lstWords_.value = v }
    val scopeFilterIndex = MutableLiveData(0)
    private val noFilter get() = textFilter.isEmpty()

    private val langWordService = LangWordService()

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilterIndex.value == 0) it.word else it.note).contains(textFilter, true))
        }
    }

    fun getData(): Observable<Unit> =
        langWordService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .map { lstWordsAll = it; applyFilters() }

    fun update(item: MLangWord): Observable<Unit> =
        langWordService.update(item)
            .applyIO()

    fun create(item: MLangWord): Observable<Unit> =
        langWordService.create(item)
            .map { item.id = it }
            .applyIO()

    fun delete(item: MLangWord): Observable<Unit> =
        langWordService.delete(item)
            .applyIO()

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    fun getNote(index: Int): Observable<Unit> {
        val item = lstWords[index]
        return vmSettings.getNote(item.word).flatMap {
            item.note = it
            langWordService.updateNote(item.id, it)
        }
    }
}
