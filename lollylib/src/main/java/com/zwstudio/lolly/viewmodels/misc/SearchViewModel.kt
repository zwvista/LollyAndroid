package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.IOnlineDict
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class SearchViewModel : ViewModel(), IOnlineDict, KoinComponent {
    var word_ = MutableStateFlow("")
    var word get() = word_.value; set(v) { word_.value = v }

    override val getWord: String get() = word
}
