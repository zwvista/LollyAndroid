package com.zwstudio.lolly.data

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.R

// https://stackoverflow.com/questions/34978872/fragment-changes-on-rotation-with-navigation-drawer
class DrawerActivityViewModel : ViewModel() {
    var menuItemId = R.id.nav_search
}
