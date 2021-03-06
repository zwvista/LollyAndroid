package com.zwstudio.lolly.data.words

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel() {

    var lstWordsAll = MutableLiveData(listOf<MLangWord>())
    var lstWords = MutableLiveData(listOf<MLangWord>())
    var isSwipeStarted = MutableLiveData(false)
    var isEditMode = MutableLiveData(false)
    var scopeFilter = MutableLiveData(SettingsViewModel.lstScopeWordFilters[0].label)
    var textFilter = MutableLiveData("")
    val noFilter get() = textFilter.value!!.isEmpty()

    @Bean
    lateinit var langWordService: LangWordService

    fun applyFilters() {
        lstWords.value = if (noFilter) lstWordsAll.value!! else lstWordsAll.value!!.filter {
            (textFilter.value!!.isEmpty() || (if (scopeFilter.value!! == "Word") it.word else it.note).contains(textFilter.value!!, true))
        }
    }

    fun getData(): Observable<Unit> =
        langWordService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .map { lstWordsAll.value = it; applyFilters() }

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
        val item = lstWords.value!![index]
        return vmSettings.getNote(item.word).flatMap {
            item.note = it
            langWordService.updateNote(item.id, it)
        }
    }
}
