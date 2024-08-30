package com.zwstudio.lolly.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

open class DrawerListViewModel : ViewModel() {
    var isEditMode_ = MutableStateFlow(false)
    var isEditMode get() = isEditMode_.value; set(v) { isEditMode_.value = v }
    var isBusy_ = MutableStateFlow(false)
    var isBusy get() = isBusy_.value; set(v) { isBusy_.value = v }
    open fun reindex(onNext: (Int) -> Unit) {}
}
