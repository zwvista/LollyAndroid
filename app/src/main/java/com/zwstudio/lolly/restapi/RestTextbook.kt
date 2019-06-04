package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MTextbooks
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbook {
    @GET("TEXTBOOKS")
    fun getDataByLang(@Query("filter") filter: String): Observable<MTextbooks>

}
