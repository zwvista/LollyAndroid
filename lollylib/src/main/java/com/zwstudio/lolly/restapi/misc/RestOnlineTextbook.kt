package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MOnlineTextbooks
import retrofit2.http.GET
import retrofit2.http.Query

interface RestOnlineTextbook {
    @GET("VONLINETEXTBOOKS")
    suspend fun getDataByLang(@Query("filter") filter: String): MOnlineTextbooks
}
