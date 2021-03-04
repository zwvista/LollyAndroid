package com.zwstudio.lolly.rest2api.misc

import com.zwstudio.lolly.domain.misc.MAutoCorrects
import retrofit2.http.GET
import retrofit2.http.Query

interface Rest2AutoCorrect {
    @GET("AUTOCORRECT")
    suspend fun getDataByLang(@Query("filter") filter: String): MAutoCorrects

}
