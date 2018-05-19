package com.zwstudio.lolly.restapi

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface RestHtml {
    @GET
    fun getStringResponse(@Url url: String): Observable<String>

}
