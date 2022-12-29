package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.misc.MReviewOptions
import com.zwstudio.lolly.models.misc.ReviewMode

class ReviewOptionsViewModel(val options: MReviewOptions) : ViewModel() {
    val mode = MutableLiveData(options.mode.ordinal)
    val interval = MutableLiveData(options.interval)
    val shuffled = MutableLiveData(options.shuffled)
    val groupCount = MutableLiveData(options.groupCount)
    val groupSelected = MutableLiveData(options.groupSelected)
    val speakingEnabled = MutableLiveData(options.speakingEnabled)
    val onRepeat = MutableLiveData(options.onRepeat)
    val moveForward = MutableLiveData(options.moveForward)
    val reviewCount = MutableLiveData(options.reviewCount)

    fun save() {
        options.mode = ReviewMode.values()[mode.value!!]
        options.interval = interval.value!!
        options.shuffled = shuffled.value!!
        options.groupCount = groupCount.value!!
        options.groupSelected = groupSelected.value!!
        options.speakingEnabled = speakingEnabled.value!!
        options.onRepeat = onRepeat.value!!
        options.moveForward = moveForward.value!!
        options.reviewCount = reviewCount.value!!
    }
}