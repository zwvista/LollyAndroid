package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object GlobalUserViewModel: ViewModel() {
    var userid_ = MutableLiveData("")
    var userid get() = userid_.value!!; set(v) { userid_.value = v }
}