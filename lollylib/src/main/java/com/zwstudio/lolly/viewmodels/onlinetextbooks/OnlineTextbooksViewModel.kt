package com.zwstudio.lolly.viewmodels.onlinetextbooks

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.misc.MOnlineTextbook
import com.zwstudio.lolly.services.misc.OnlineTextbookService
import com.zwstudio.lolly.viewmodels.LollyListViewModel
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnlineTextbooksViewModel : LollyListViewModel(), KoinComponent {

    var lstOnlineTextbooksAll_ = MutableStateFlow(listOf<MOnlineTextbook>())
    var lstOnlineTextbooksAll get() = lstOnlineTextbooksAll_.value; set(v) { lstOnlineTextbooksAll_.value = v }
    var lstOnlineTextbooks_ = MutableStateFlow(listOf<MOnlineTextbook>())
    var lstOnlineTextbooks get() = lstOnlineTextbooks_.value; set(v) { lstOnlineTextbooks_.value = v }
    val onlineTextbookFilterIndex_ = MutableStateFlow(0)
    var onlineTextbookFilterIndex get() = onlineTextbookFilterIndex_.value; set(v) { onlineTextbookFilterIndex_.value = v }
    private val onlineTextbookFilter get() = vmSettings.lstOnlineTextbookFilters[onlineTextbookFilterIndex].value
    private val noFilter get() = onlineTextbookFilter == 0

    private val onlineTextbookService by inject<OnlineTextbookService>()

    init {
        combine(lstOnlineTextbooksAll_, onlineTextbookFilterIndex_, ::Pair).onEach {
            lstOnlineTextbooks = if (noFilter) lstOnlineTextbooksAll else lstOnlineTextbooksAll.filter {
                onlineTextbookFilter == 0 || it.textbookid == onlineTextbookFilter
            }
        }.launchIn(viewModelScope)
    }

    fun getData(): Completable {
        isBusy = true
        return onlineTextbookService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstOnlineTextbooksAll = it; isBusy = false; Completable.complete() }
    }
}
