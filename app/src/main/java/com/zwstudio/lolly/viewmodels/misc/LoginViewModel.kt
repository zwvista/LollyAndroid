package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.services.misc.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService by inject<UserService>()

    suspend fun login(): String =
        userService.getData(username.value!!, password.value!!)
            .let { if (it.isEmpty()) "" else it[0].userid }
}