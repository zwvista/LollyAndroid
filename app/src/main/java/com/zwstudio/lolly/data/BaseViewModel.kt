package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.App
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

fun <T> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

@EBean
class BaseViewModel1 {
    @App
    lateinit var app: LollyApplication
}

@EBean
class BaseViewModel2 : BaseViewModel1() {
    @Bean
    lateinit var vmSettings: SettingsViewModel
}
