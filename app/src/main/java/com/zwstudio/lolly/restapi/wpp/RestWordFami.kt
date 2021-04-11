package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.wpp.MWordsFami
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestWordFami {
    @GET("WORDSFAMI")
    fun getDataByUserWord(@Query("filter") vararg filters: String): Observable<MWordsFami>

    @FormUrlEncoded
    @PUT("WORDSFAMI/{id}")
    fun update(@Path("id") id: Int, @Field("USERID") userid: String,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Observable<Int>

    @FormUrlEncoded
    @POST("WORDSFAMI")
    fun create(@Field("USERID") userid: String,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Observable<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
