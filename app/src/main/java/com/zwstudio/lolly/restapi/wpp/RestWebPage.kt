package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.wpp.MWebPages
import retrofit2.http.*

interface RestWebPage {
    @GET("WEBPAGES")
    suspend fun getDataBySearch(@Query("filter") vararg filters: String): MWebPages

    @GET("WEBPAGES")
    suspend fun getDataById(@Query("filter") filter: String): MWebPages

    @FormUrlEncoded
    @PUT("WEBPAGES/{id}")
    suspend fun update(@Path("id") id: Int,
               @Field("TITLE") title: String, @Field("URL") url: String): Int

    @FormUrlEncoded
    @POST("WEBPAGES")
    suspend fun create(@Field("TITLE") title: String, @Field("URL") url: String): Int

    @DELETE("WEBPAGES/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}
