package com.zwstudio.lolly.rest2api.wpp

import com.zwstudio.lolly.domain.wpp.MWordsFami
import retrofit2.http.*

interface Rest2WordFami {
    @GET("WORDSFAMI")
    suspend fun getDataByUserWord(@Query("filter") vararg filters: String): MWordsFami

    @FormUrlEncoded
    @PUT("WORDSFAMI/{id}")
    suspend fun update(@Path("id") id: Int, @Field("USERID") userid: Int,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Int

    @FormUrlEncoded
    @POST("WORDSFAMI")
    suspend fun create(@Field("USERID") userid: Int,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Int

    @DELETE("UNITWORDS/{id}")
    suspend fun delete(@Path("id") id: Int): Int

}
