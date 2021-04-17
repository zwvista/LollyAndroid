package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.services.misc.UserService
import io.reactivex.rxjava3.core.Observable

class LoginViewModel : ViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService = UserService()

    fun login(): Observable<String> =
        userService.getData(username.value!!, password.value!!)
            .map { if (it.isEmpty()) "" else it[0].userid }
            .applyIO()
}