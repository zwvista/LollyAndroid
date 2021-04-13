package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MAutoCorrects
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAutoCorrect {
    @GET("AUTOCORRECT")
    suspend fun getDataByLang(@Query("filter") filter: String): MAutoCorrects

}
