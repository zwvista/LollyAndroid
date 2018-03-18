package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.TextbookWords
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbookWord {
    @GET("VTEXTBOOKWORDS?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<TextbookWords>

}
