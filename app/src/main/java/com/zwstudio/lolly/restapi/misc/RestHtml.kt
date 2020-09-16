package com.zwstudio.lolly.restapi.misc

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface RestHtml {
    @GET
    fun getStringResponse(@Url url: String): Observable<String>

}
