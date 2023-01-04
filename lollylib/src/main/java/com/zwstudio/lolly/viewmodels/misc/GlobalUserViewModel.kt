package com.zwstudio.lolly.viewmodels.misc

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

object GlobalUserViewModel: ViewModel() {
    var userid_ = MutableStateFlow("")
    var userid: String
        get() = userid_.value
        set(v) {
            userid_.value = v
            isLoggedIn = userid.isNotEmpty()
        }
    val isLoggedIn_ = MutableStateFlow(false)
    var isLoggedIn get() = isLoggedIn_.value; set(v) { isLoggedIn_.value = v }
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
    fun remove(context: Context) {
        userid = ""
        context.getSharedPreferences("users", 0)
            .edit()
            .remove("userid")
            .apply()
    }
}