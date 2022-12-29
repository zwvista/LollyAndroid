package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MLanguages
import retrofit2.http.GET

interface RestLanguage {
    @GET("LANGUAGES?filter=ID,neq,0")
    suspend fun getData(): MLanguages
}
