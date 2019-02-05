package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.LangWords
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestLangWord {
    @GET("LANGWORDS?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<LangWords>

    @GET("LANGWORDS?transform=1")
    fun getDataByLangWord(@Query("filter[]") vararg filters: String): Observable<LangWords>

    @GET("LANGWORDS?transform=1")
    fun getDataById(@Query("filter") filter: String): Observable<LangWords>

}
