package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MWordsFami
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestWordFami {
    @GET("WORDSFAMI")
    fun getDataByUserWord(@Query("filter") vararg filters: String): Observable<MWordsFami>

    @FormUrlEncoded
    @PUT("WORDSFAMI/{id}")
    fun update(@Path("id") id: Int, @Field("USERID") userid: Int,
               @Field("WORDID") wordid: Int, @Field("LEVEL") level: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Observable<Int>

    @FormUrlEncoded
    @POST("WORDSFAMI")
    fun create(@Field("USERID") userid: Int,
               @Field("WORDID") wordid: Int, @Field("LEVEL") level: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Observable<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
