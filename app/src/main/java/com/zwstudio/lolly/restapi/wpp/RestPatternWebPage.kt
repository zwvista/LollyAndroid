package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MPatternWebPages
import retrofit2.http.*

interface RestPatternWebPage {
    @GET("VPATTERNSWEBPAGES?order=SEQNUM")
    suspend fun getDataByPattern(@Query("filter") filter: String): MPatternWebPages

    @GET("VPATTERNSWEBPAGES")
    suspend fun getDataById(@Query("filter") filter: String): MPatternWebPages

    @FormUrlEncoded
    @PUT("PATTERNSWEBPAGES/{id}")
    suspend fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Int

    @FormUrlEncoded
    @PUT("PATTERNSWEBPAGES/{id}")
    suspend fun update(@Path("id") id: Int, @Field("PATTERNID") patternid: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WEBPAGEID") webpageid: Int): Int

    @FormUrlEncoded
    @POST("PATTERNSWEBPAGES")
    suspend fun create(@Field("PATTERNID") patternid: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WEBPAGEID") webpageid: Int): Int

    @DELETE("PATTERNSWEBPAGES/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}
