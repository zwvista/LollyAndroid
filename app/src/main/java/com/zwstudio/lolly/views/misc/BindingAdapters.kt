package com.zwstudio.lolly.views.misc

import android.view.View
import androidx.databinding.BindingAdapter

// https://stackoverflow.com/questions/44420396/use-data-binding-to-set-view-visibility
@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}
