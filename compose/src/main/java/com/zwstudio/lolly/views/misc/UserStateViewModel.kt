package com.zwstudio.lolly.views.misc

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

// https://nanosoft.co.za/blog/post/login-logout-flow-with-android-jetpack-compose-and-compositionlocal

class UserStateViewModel : ViewModel() {
    var user by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)

    suspend fun signIn(email: String, password: String) {
        isBusy = true
        delay(2000)
        isLoggedIn = true
        isBusy = false
    }

    suspend fun signOut() {
        isBusy = true
        delay(2000)
        isLoggedIn = false
        isBusy = false
    }
}

val LocalUserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }