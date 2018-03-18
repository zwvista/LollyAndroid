package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.Textbooks
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbook {
    @GET("TEXTBOOKS?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<Textbooks>

}
