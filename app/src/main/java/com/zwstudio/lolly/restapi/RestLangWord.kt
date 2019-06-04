package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MLangWords
import io.reactivex.Observable
import retrofit2.http.*

interface RestLangWord {
    @GET("VLANGWORDS?order=WORD")
    fun getDataByLang(@Query("filter") filter: String): Observable<MLangWords>

    @GET("VLANGWORDS")
    fun getDataByLangWord(@Query("filter") vararg filters: String): Observable<MLangWords>

    @GET("VLANGWORDS")
    fun getDataById(@Query("filter") filter: String): Observable<MLangWords>

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Observable<Int>

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("NOTE") note: String?): Observable<Int>

    @FormUrlEncoded
    @POST("LANGWORDS")
    fun create(@Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("NOTE") note: String?): Observable<Int>

    @DELETE("LANGWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
