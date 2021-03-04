package com.zwstudio.lolly.rest2api.misc

import com.zwstudio.lolly.domain.misc.MUSMappings
import retrofit2.http.GET

interface Rest2USMapping {
    @GET("USMAPPINGS")
    suspend fun getData(): MUSMappings
}
