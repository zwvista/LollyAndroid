package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.LollyApplication
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit

@EBean
class BaseService {
    val retrofitJson: Retrofit
        get() = LollyApplication.retrofitJson
    val retrofitSP: Retrofit
        get() = LollyApplication.retrofitSP
    val retrofitHtml: Retrofit
        get() = LollyApplication.retrofitHtml
}