package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode
import kotlinx.coroutines.flow.MutableStateFlow

class ReviewOptionsViewModel(val options: MReviewOptions) : ViewModel() {
    val mode = MutableStateFlow(options.mode.ordinal)
    val interval = MutableStateFlow(options.interval)
    val shuffled = MutableStateFlow(options.shuffled)
    val groupCount = MutableStateFlow(options.groupCount)
    val groupSelected = MutableStateFlow(options.groupSelected)
    val speakingEnabled = MutableStateFlow(options.speakingEnabled)
    val onRepeat = MutableStateFlow(options.onRepeat)
    val moveForward = MutableStateFlow(options.moveForward)
    val reviewCount = MutableStateFlow(options.reviewCount)

    fun save() {
        options.mode = ReviewMode.values()[mode.value]
        options.interval = interval.value
        options.shuffled = shuffled.value
        options.groupCount = groupCount.value
        options.groupSelected = groupSelected.value
        options.speakingEnabled = speakingEnabled.value
        options.onRepeat = onRepeat.value
        options.moveForward = moveForward.value
        options.reviewCount = reviewCount.value
    }
}