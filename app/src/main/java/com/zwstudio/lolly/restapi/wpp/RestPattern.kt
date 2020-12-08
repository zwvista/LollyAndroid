package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.misc.MSPResult
import com.zwstudio.lolly.domain.wpp.MPatterns
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestPattern {
    @GET("PATTERNS?order=PATTERN")
    fun getDataByLang(@Query("filter") filter: String): Observable<MPatterns>

    @GET("PATTERNS")
    fun getDataById(@Query("filter") filter: String): Observable<MPatterns>

    @FormUrlEncoded
    @PUT("PATTERNS/{id}")
    fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String): Observable<Int>

    @FormUrlEncoded
    @PUT("PATTERNS/{id}")
    fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("PATTERN") pattern: String, @Field("NOTE") note: String,
               @Field("TAGS") tags: String): Observable<Int>

    @FormUrlEncoded
    @POST("PATTERNS")
    fun create(@Field("LANGID") langid: Int,
               @Field("PATTERN") pattern: String, @Field("NOTE") note: String,
               @Field("TAGS") tags: String): Observable<Int>

    @DELETE("PATTERNS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

    @FormUrlEncoded
    @POST("PATTERNS_MERGE")
    fun mergePatterns(@Field("P_IDS_MERGE") idsMerge: String, @Field("P_PATTERN") pattern: String,
                      @Field("P_NOTE") note: String, @Field("P_TAGS") tags: String): Observable<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("PATTERNS_SPLIT")
    fun splitPattern(@Field("P_ID") id: Int, @Field("P_PATTERNS_SPLIT") patternsSplit: String): Observable<List<List<MSPResult>>>

}
