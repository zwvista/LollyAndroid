package com.zwstudio.lolly.data.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    val username = MutableLiveData("")
    val password = MutableLiveData("")
}