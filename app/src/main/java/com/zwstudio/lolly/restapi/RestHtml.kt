package com.zwstudio.lolly.restapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface RestHtml {
    @GET
    fun getStringResponse(@Url url: String): Call<String>

}
