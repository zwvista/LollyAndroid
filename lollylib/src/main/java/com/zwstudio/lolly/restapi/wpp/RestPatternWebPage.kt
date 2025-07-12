package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.models.wpp.MPatternWebPages
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestPatternWebPage {
    @GET("VPATTERNSWEBPAGES?order=SEQNUM")
    suspend fun getDataByPattern(@Query("filter") filter: String): MPatternWebPages

    @GET("VPATTERNSWEBPAGES")
    suspend fun getDataById(@Query("filter") filter: String): MPatternWebPages

    @FormUrlEncoded
    @PUT("PATTERNSWEBPAGES/{id}")
    suspend fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Int

    @PUT("PATTERNSWEBPAGES/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MPatternWebPage): Int

    @POST("PATTERNSWEBPAGES")
    suspend fun create(@Body item: MPatternWebPage): Int

    @DELETE("PATTERNSWEBPAGES/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}
