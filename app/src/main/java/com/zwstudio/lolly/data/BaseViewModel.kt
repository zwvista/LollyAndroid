package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class BaseViewModel {
    @App
    lateinit var app: LollyApplication
    @Bean
    lateinit var vm: SettingsViewModel

}