package com.zwstudio.lolly.data;

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.data.misc.BaseViewModel

class DrawerListViewModel : BaseViewModel() {
    val isSwipeStarted_ = MutableLiveData(false)
    var isSwipeStarted get() = isSwipeStarted_.value!!; set(v) { isSwipeStarted_.value = v }
    var isEditMode_ = MutableLiveData(false)
    var isEditMode get() = isEditMode_.value!!; set(v) { isEditMode_.value = v }
    var textFilter_ = MutableLiveData("")
    var textFilter get() = textFilter_.value!!; set(v) { textFilter_.value = v }
    fun reindex(onNext: (Int) -> Unit) {}
}
