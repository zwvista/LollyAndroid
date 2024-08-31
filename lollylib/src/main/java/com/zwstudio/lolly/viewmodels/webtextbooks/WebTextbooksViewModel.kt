package com.zwstudio.lolly.viewmodels.webtextbooks

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.misc.MWebTextbook
import com.zwstudio.lolly.services.misc.WebTextbookService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WebTextbooksViewModel : DrawerListViewModel(), KoinComponent {

    var lstWebTextbooksAll_ = MutableStateFlow(listOf<MWebTextbook>())
    var lstWebTextbooksAll get() = lstWebTextbooksAll_.value; set(v) { lstWebTextbooksAll_.value = v }
    var lstWebTextbooks_ = MutableStateFlow(listOf<MWebTextbook>())
    var lstWebTextbooks get() = lstWebTextbooks_.value; set(v) { lstWebTextbooks_.value = v }
    val webTextbookFilterIndex_ = MutableStateFlow(0)
    var webTextbookFilterIndex get() = webTextbookFilterIndex_.value; set(v) { webTextbookFilterIndex_.value = v }
    private val webTextbookFilter get() = vmSettings.lstWebTextbookFilters[webTextbookFilterIndex].value
    private val noFilter get() = webTextbookFilter == 0

    private val webTextbookService by inject<WebTextbookService>()

    init {
        combine(lstWebTextbooksAll_, webTextbookFilterIndex_, ::Pair).onEach {
            lstWebTextbooks = if (noFilter) lstWebTextbooksAll else lstWebTextbooksAll.filter {
                webTextbookFilter == 0 || it.textbookid == webTextbookFilter
            }
        }.launchIn(viewModelScope)
    }

    fun getData(): Completable {
        isBusy = true
        return webTextbookService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstWebTextbooksAll = it; isBusy = false; Completable.complete() }
    }
}
