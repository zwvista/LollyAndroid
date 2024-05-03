package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MWordsFami
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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
