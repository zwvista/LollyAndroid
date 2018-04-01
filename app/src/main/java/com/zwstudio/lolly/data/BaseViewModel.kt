package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit
import java.io.Serializable

@EBean
class BaseViewModel1 : Serializable {
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
