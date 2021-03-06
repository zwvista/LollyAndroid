package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.service.wpp.LangWordService
import com.zwstudio.lolly.service.wpp.WordFamiService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class WordsLangViewModel : BaseViewModel() {

    var lstWordsAll = listOf<MLangWord>()
    var lstWords = listOf<MLangWord>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopeWordFilters[0].label
    var textFilter = ""
    val noFilter get() = textFilter.isEmpty()

    @Bean
    lateinit var langWordService: LangWordService
    @Bean
    lateinit var wordFamiService: WordFamiService

    fun applyFilters() {
        lstWords = if (noFilter) lstWordsAll else lstWordsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Word") it.word else it.note).contains(textFilter, true))
        }
    }

    suspend fun getData() {
        val lst = langWordService.getDataByLang(vmSettings.selectedLang.id)
        withContext(Dispatchers.Main) { lstWordsAll = lst; applyFilters() }
    }

    suspend fun update(item: MLangWord) =
        langWordService.update(item)

    suspend fun create(item: MLangWord) {
        item.id = langWordService.create(item)
    }

    suspend fun delete(item: MLangWord) =
        langWordService.delete(item)

    fun newLangWord() = MLangWord().apply {
        langid = vmSettings.selectedLang.id
    }

    suspend fun getNote(index: Int) {
        val item = lstWords[index]
        item.note = vmSettings.getNote(item.word)
        langWordService.updateNote(item.id, item.note)
    }
}
