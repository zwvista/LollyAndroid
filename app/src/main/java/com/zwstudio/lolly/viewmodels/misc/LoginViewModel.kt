package com.zwstudio.lolly.viewmodels.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.services.misc.UserService
import com.zwstudio.lolly.views.applyIO
import io.reactivex.rxjava3.core.Single
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService by inject<UserService>()

    fun login(): Single<String> =
        userService.getData(username.value!!, password.value!!)
            .map { if (it.isEmpty()) "" else it[0].userid }
            .applyIO()
}