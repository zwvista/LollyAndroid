package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.misc.MSPResult
import com.zwstudio.lolly.models.wpp.MPatterns
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestPattern {
    @GET("PATTERNS?order=PATTERN")
    suspend fun getDataByLang(@Query("filter") filter: String): MPatterns

    @GET("PATTERNS")
    suspend fun getDataById(@Query("filter") filter: String): MPatterns

    @FormUrlEncoded
    @PUT("PATTERNS/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String): Int

    @FormUrlEncoded
    @PUT("PATTERNS/{id}")
    suspend fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("PATTERN") pattern: String,
               @Field("TAGS") tags: String,
               @Field("TITLE") title: String,
               @Field("URL") url: String): Int

    @FormUrlEncoded
    @POST("PATTERNS")
    suspend fun create(@Field("LANGID") langid: Int,
               @Field("PATTERN") pattern: String,
               @Field("TAGS") tags: String,
               @Field("TITLE") title: String,
               @Field("URL") url: String): Int

    @DELETE("PATTERNS/{id}")
    suspend fun delete(@Path("id") id: Int): Int

    @FormUrlEncoded
    @POST("PATTERNS_MERGE")
    suspend fun mergePatterns(@Field("P_IDS_MERGE") idsMerge: String, @Field("P_PATTERN") pattern: String,
                      @Field("P_NOTE") note: String, @Field("P_TAGS") tags: String): List<List<MSPResult>>

    @FormUrlEncoded
    @POST("PATTERNS_SPLIT")
    suspend fun splitPattern(@Field("P_ID") id: Int, @Field("P_PATTERNS_SPLIT") patternsSplit: String): List<List<MSPResult>>

}
