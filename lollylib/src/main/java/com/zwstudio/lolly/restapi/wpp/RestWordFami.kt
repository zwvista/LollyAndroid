package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MWordsFami
import io.reactivex.rxjava3.core.Single
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
    fun getDataByUserWord(@Query("filter") vararg filters: String): Single<MWordsFami>

    @FormUrlEncoded
    @PUT("WORDSFAMI/{id}")
    fun update(@Path("id") id: Int, @Field("USERID") userid: String,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Single<Int>

    @FormUrlEncoded
    @POST("WORDSFAMI")
    fun create(@Field("USERID") userid: String,
               @Field("WORDID") wordid: Int,
               @Field("CORRECT") correct: Int, @Field("TOTAL") total: Int): Single<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Single<Int>

}
