package com.zwstudio.lolly.data.misc

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.LollyApplication
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.androidannotations.annotations.EBean

fun <T> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

@EBean
class BaseViewModel : ViewModel() {
    val vmSettings: SettingsViewModel
        get() = LollyApplication.vmSettings
}
