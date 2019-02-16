package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.LangWords
import io.reactivex.Observable
import retrofit2.http.*

interface RestLangWord {
    @GET("VLANGWORDS?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<LangWords>

    @GET("VLANGWORDS?transform=1")
    fun getDataByLangWord(@Query("filter[]") vararg filters: String): Observable<LangWords>

    @GET("VLANGWORDS?transform=1")
    fun getDataById(@Query("filter") filter: String): Observable<LangWords>

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Observable<Int>

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("LEVEL") level: Int,
               @Field("NOTE") note: String?): Observable<Int>

    @FormUrlEncoded
    @POST("LANGWORDS")
    fun create(@Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("LEVEL") level: Int,
               @Field("NOTE") note: String?): Observable<Int>

    @DELETE("LANGWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
