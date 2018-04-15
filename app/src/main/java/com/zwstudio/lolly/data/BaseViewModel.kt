package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import com.zwstudio.lolly.restapi.RestHtml
import org.androidannotations.annotations.App
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@EBean
class BaseViewModel1 {
    @App
    lateinit var app: LollyApplication
    val retrofitJson: Retrofit
        get() = app.retrofitJson
    val retrofitHtml: Retrofit
        get() = app.retrofitHtml

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    fun getHtml(url: String, onNext: (String) -> Unit) {
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>?, response: Response<String>?) {
                    if (response!!.isSuccessful())
                        onNext(response.body()!!)
                }

                override fun onFailure(call: Call<String>?, t: Throwable?) {

                }
            })
    }
}

@EBean
class BaseViewModel2 : BaseViewModel1() {
    @Bean
    lateinit var vmSettings: SettingsViewModel
}
