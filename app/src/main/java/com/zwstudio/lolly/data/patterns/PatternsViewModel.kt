package com.zwstudio.lolly.data.patterns

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.SettingsViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.service.wpp.PatternService
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsViewModel : BaseViewModel() {

    var lstPatternsAll = listOf<MPattern>()
    var lstPatterns = listOf<MPattern>()
    var isSwipeStarted = false
    var isEditMode = false
    var scopeFilter = SettingsViewModel.lstScopePatternFilters[0].label
    var textFilter = ""
    val noFilter get() = textFilter.isEmpty()

    @Bean
    lateinit var patternService: PatternService

    fun applyFilters() {
        lstPatterns = if (noFilter) lstPatternsAll else lstPatternsAll.filter {
            (textFilter.isEmpty() || (if (scopeFilter == "Pattern") it.pattern else if (scopeFilter == "Note") it.note else it.tags).contains(textFilter, true))
        }
    }

    suspend fun getData() {
        val lst = patternService.getDataByLang(vmSettings.selectedLang.id)
        withContext(Dispatchers.Main) { lstPatternsAll = lst; applyFilters() }
    }

    suspend fun update(item: MPattern) =
        patternService.update(item)

    suspend fun create(item: MPattern) {
        item.id = patternService.create(item)
    }

    suspend fun delete(id: Int) =
        patternService.delete(id)

    fun newPattern() = MPattern().apply {
        langid = vmSettings.selectedLang.id
    }

}
