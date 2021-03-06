package com.zwstudio.lolly.restapi.misc

import retrofit2.http.GET
import retrofit2.http.Url


interface RestHtml {
    @GET
    suspend fun getStringResponse(@Url url: String): String

}
