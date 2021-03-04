package com.zwstudio.lolly.rest2api.misc

import com.zwstudio.lolly.domain.misc.MVoices
import retrofit2.http.GET
import retrofit2.http.Query

interface Rest2Voice {
    @GET("VVOICES")
    suspend fun getDataByLang(@Query("filter") vararg filters: String): MVoices
}
