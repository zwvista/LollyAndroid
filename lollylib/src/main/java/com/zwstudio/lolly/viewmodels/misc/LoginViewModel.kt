package com.zwstudio.lolly.viewmodels.misc

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.services.misc.UserService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService by inject<UserService>()

    suspend fun login(context: Context): Boolean =
        userService.getData(username.value!!, password.value!!)
            .let {
                if (it.isEmpty()) {
                    GlobalUserViewModel.save(context, "")
                    false
                }
                else {
                    GlobalUserViewModel.save(context, it[0].userid)
                    true
                }
            }
}