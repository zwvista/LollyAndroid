package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit

@EBean
class BaseViewModel1 {
    @App
    lateinit var app: LollyApplication
    val retrofit: Retrofit
        get() = app.retrofit
}

@EBean
class BaseViewModel2 : BaseViewModel1() {
    @Bean
    lateinit var vmSettings: SettingsViewModel
}
