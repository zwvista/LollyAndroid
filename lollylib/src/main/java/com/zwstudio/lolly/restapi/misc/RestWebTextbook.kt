package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MWebTextbooks
import retrofit2.http.GET
import retrofit2.http.Query

interface RestWebTextbook {
    @GET("VWEBTEXTBOOKS")
    suspend fun getDataByLang(@Query("filter") filter: String): MWebTextbooks
}
