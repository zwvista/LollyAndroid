package com.zwstudio.lolly.rest2api.misc

import com.zwstudio.lolly.domain.misc.MLanguages
import retrofit2.http.GET

interface Rest2Language {
    @GET("LANGUAGES?filter=ID,neq,0")
    suspend fun getData(): MLanguages
}
