package com.zwstudio.lolly.data.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.applyIO
import com.zwstudio.lolly.service.misc.UserService

class LoginViewModel : ViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService = UserService()

    suspend fun login(): String =
        userService.getData(username.value!!, password.value!!)
            .let { if (it.isEmpty()) "" else it[0].userid }
}