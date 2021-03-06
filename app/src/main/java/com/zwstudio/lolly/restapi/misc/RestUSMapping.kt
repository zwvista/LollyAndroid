package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MUSMappings
import retrofit2.http.GET

interface RestUSMapping {
    @GET("USMAPPINGS")
    suspend fun getData(): MUSMappings
}
