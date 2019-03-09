package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MAutoCorrects
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAutoCorrect {
    @GET("AUTOCORRECT?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<MAutoCorrects>

}
