package com.zwstudio.lolly.viewmodels.misc

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object GlobalUserViewModel: ViewModel() {
    var userid_ = MutableLiveData("")
    var userid get() = userid_.value!!; set(v) { userid_.value = v }
    val isLoggedIn: Boolean get() = userid.isNotEmpty()
    fun load(context: Context) {
        userid = context.getSharedPreferences("users", 0).getString("userid", "")!!
    }
    fun save(context: Context, id: String) {
        userid = id
        context.getSharedPreferences("users", 0)
            .edit()
            .putString("userid", userid)
            .apply()
    }
}