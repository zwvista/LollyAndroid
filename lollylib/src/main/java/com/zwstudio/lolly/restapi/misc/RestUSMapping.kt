package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MUSMappings
import retrofit2.http.GET

interface RestUSMapping {
    @GET("USMAPPINGS")
    suspend fun getData(): MUSMappings
}
