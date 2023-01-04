package com.zwstudio.lolly.viewmodels.misc

import android.content.Context
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.services.misc.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val userService by inject<UserService>()

    suspend fun login(context: Context) =
        userService.getData(username.value, password.value).let {
            val id = if (it.isEmpty()) "" else it[0].userid
            GlobalUserViewModel.save(context, id)
        }
}