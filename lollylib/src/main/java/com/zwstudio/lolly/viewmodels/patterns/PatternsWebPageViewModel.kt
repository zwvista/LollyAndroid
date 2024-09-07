package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.IOnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.misc.MDictionary
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class PatternsWebPageViewModel : ViewModel(), KoinComponent {
    var lstPatterns_ = MutableStateFlow(listOf<String>())
    var lstPatterns get() = lstPatterns_.value; set(v) { lstPatterns_.value = v }
    var selectedPatternIndex_ = MutableStateFlow(0)
    var selectedPatternIndex get() = selectedPatternIndex_.value; set(v) { selectedPatternIndex_.value = v }
    val selectedPattern: String
        get() = lstPatterns[selectedPatternIndex]

    fun next(delta: Int) {
        selectedPatternIndex = (selectedPatternIndex + delta + lstPatterns.size) % lstPatterns.size
    }
}
