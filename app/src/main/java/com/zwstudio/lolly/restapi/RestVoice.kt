package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MVoice
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestVoice {
    @GET("VVOICES?transform=1")
    fun getDataByLang(@Query("filter[]") vararg filters: String): Observable<MVoice>
}
