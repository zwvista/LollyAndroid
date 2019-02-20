package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.WordsFami
import io.reactivex.Observable
import retrofit2.http.*

interface RestWordFami {
    @GET("WORDSFAMI?transform=1")
    fun getDataByUserWord(@Query("filter[]") vararg filters: String): Observable<WordsFami>

    @FormUrlEncoded
    @PUT("WORDSFAMI/{id}")
    fun update(@Path("id") id: Int, @Field("USERID") userid: Int,
               @Field("WORDID") wordid: Int, @Field("LEVEL") level: Int): Observable<Int>

    @FormUrlEncoded
    @POST("WORDSFAMI")
    fun create(@Field("USERID") userid: Int,
               @Field("WORDID") wordid: Int, @Field("LEVEL") level: Int): Observable<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
