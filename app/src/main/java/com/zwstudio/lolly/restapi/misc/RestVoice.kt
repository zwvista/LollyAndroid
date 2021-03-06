package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MVoices
import retrofit2.http.GET
import retrofit2.http.Query

interface RestVoice {
    @GET("VVOICES")
    suspend fun getDataByLang(@Query("filter") vararg filters: String): MVoices
}
