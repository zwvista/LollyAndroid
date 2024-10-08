package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MPattern
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class PatternsWebPageViewModel(val lstPatterns: List<MPattern>, index: Int) : ViewModel(), KoinComponent {
    var selectedPatternIndex_ = MutableStateFlow(index)
    var selectedPatternIndex get() = selectedPatternIndex_.value; set(v) { selectedPatternIndex_.value = v }
    val selectedPattern: MPattern
        get() = lstPatterns[selectedPatternIndex]

    fun next(delta: Int) {
        selectedPatternIndex = (selectedPatternIndex + delta + lstPatterns.size) % lstPatterns.size
    }
}
