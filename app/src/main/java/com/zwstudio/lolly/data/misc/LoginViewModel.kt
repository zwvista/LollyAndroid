package com.zwstudio.lolly.data.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.service.misc.UserService
import io.reactivex.rxjava3.core.Observable

class LoginViewModel : ViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService = UserService()

    fun login(): Observable<Int> =
        userService.getData(username.value!!, password.value!!)
            .map { if (it.isEmpty()) 0 else it[0].id }
}