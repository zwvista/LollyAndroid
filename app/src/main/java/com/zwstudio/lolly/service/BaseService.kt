package com.zwstudio.lolly.service

import com.zwstudio.lolly.android.LollyApplication
import org.androidannotations.annotations.App
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit

@EBean
class BaseService {
    @App
    lateinit var app: LollyApplication
    val retrofitJson: Retrofit
        get() = app.retrofitJson
    val retrofitSP: Retrofit
        get() = app.retrofitSP
    val retrofitHtml: Retrofit
        get() = app.retrofitHtml
}