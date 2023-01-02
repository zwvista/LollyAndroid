package com.zwstudio.lolly.viewmodels.misc

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.services.misc.UserService
import io.reactivex.rxjava3.core.Completable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {
    val username = MutableLiveData("")
    val password = MutableLiveData("")

    private val userService by inject<UserService>()

    fun login(context: Context): Completable =
        userService.getData(username.value!!, password.value!!)
            .flatMapCompletable {
                val id = if (it.isEmpty()) "" else it[0].userid
                GlobalUserViewModel.save(context, id)
                Completable.complete()
            }.applyIO()
}