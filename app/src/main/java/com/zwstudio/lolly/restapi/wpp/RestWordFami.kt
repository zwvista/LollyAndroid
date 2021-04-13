package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MWordsFami
import retrofit2.http.*

interface RestWordFami {
    @GET("WORDSFAMI")
    suspend fun getDataByUserWord(@Query("filter") vararg filters: String): MWordsFami

    @FormUrlEncoded
    @PUT("WORDSFAMI/{id}")
    suspend fun update(@Path("id") id: Int, @Field("USERID") userid: String,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Int

    @FormUrlEncoded
    @POST("WORDSFAMI")
    suspend fun create(@Field("USERID") userid: String,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Int

    @DELETE("UNITWORDS/{id}")
    suspend fun delete(@Path("id") id: Int): Int

}
