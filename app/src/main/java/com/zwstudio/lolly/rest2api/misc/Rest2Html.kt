package com.zwstudio.lolly.rest2api.misc

import retrofit2.http.GET
import retrofit2.http.Url


interface Rest2Html {
    @GET
    suspend fun getStringResponse(@Url url: String): String

}
