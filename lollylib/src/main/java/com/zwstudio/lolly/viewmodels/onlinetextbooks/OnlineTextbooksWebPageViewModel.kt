package com.zwstudio.lolly.viewmodels.onlinetextbooks

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.misc.MOnlineTextbook
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class OnlineTextbooksWebPageViewModel(val lstOnlineTextbooks: List<MOnlineTextbook>, index: Int) : ViewModel(), KoinComponent {
    var selectedOnlineTextbookIndex_ = MutableStateFlow(index)
    var selectedOnlineTextbookIndex get() = selectedOnlineTextbookIndex_.value; set(v) { selectedOnlineTextbookIndex_.value = v }
    val selectedOnlineTextbook: MOnlineTextbook
        get() = lstOnlineTextbooks[selectedOnlineTextbookIndex]

    fun next(delta: Int) {
        selectedOnlineTextbookIndex = (selectedOnlineTextbookIndex + delta + lstOnlineTextbooks.size) % lstOnlineTextbooks.size
    }
}
