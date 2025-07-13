package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.vmSettings
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class UnitBlogPostsViewModel: ViewModel(), KoinComponent {
    var selectedUnitIndex_ = MutableStateFlow(vmSettings.selectedUnitToIndex)
    var selectedUnitIndex get() = selectedUnitIndex_.value; set(v) { selectedUnitIndex_.value = v }
    val lstUnits = vmSettings.lstUnits
    val selectedUnit: Int
        get() = lstUnits[selectedUnitIndex].value

    fun next(delta: Int) {
        selectedUnitIndex = (selectedUnitIndex + delta + lstUnits.size) % lstUnits.size
    }
}
