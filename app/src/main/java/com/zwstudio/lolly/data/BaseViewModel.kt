package com.zwstudio.lolly.data

import com.zwstudio.lolly.android.LollyApplication
import com.zwstudio.lolly.restapi.RestHtml
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.androidannotations.annotations.App
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean
import retrofit2.Retrofit

fun <T> Observable<T>.applyIO(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

@EBean
class BaseViewModel1 {
    @App
    lateinit var app: LollyApplication
    val retrofitJson: Retrofit
        get() = app.retrofitJson
    val retrofitHtml: Retrofit
        get() = app.retrofitHtml

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    fun getHtml(url: String): Observable<String> =
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
            .applyIO()
}

@EBean
class BaseViewModel2 : BaseViewModel1() {
    @Bean
    lateinit var vmSettings: SettingsViewModel
}
