package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.LollyApplication
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit

@EBean
class BaseService {
    val retrofitJson2: Retrofit
        get() = LollyApplication.retrofitJson2
    val retrofitSP2: Retrofit
        get() = LollyApplication.retrofitSP2
    val retrofitHtml2: Retrofit
        get() = LollyApplication.retrofitHtml2
}