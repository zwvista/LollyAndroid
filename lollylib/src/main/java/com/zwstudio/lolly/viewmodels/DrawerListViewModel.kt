package com.zwstudio.lolly.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class DrawerListViewModel : ViewModel() {
    val isSwipeStarted_ = MutableStateFlow(false)
    var isSwipeStarted get() = isSwipeStarted_.value; set(v) { isSwipeStarted_.value = v }
    var isEditMode_ = MutableStateFlow(false)
    var isEditMode get() = isEditMode_.value; set(v) { isEditMode_.value = v }
    var textFilter_ = MutableStateFlow("")
    var textFilter get() = textFilter_.value; set(v) { textFilter_.value = v }
    open fun reindex(onNext: (Int) -> Unit) {}
}
