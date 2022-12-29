package com.zwstudio.suspendapi.restapi.misc

import com.zwstudio.lolly.models.misc.MTextbooks
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbook {
    @GET("TEXTBOOKS")
    suspend fun getDataByLang(@Query("filter") filter: String): MTextbooks

}
