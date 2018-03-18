package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.Dictionaries
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictionary {
    @GET("VDICTIONARIES?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<Dictionaries>

}
