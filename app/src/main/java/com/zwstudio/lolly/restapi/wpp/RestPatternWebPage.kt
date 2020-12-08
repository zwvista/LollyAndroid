package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.wpp.MPatternWebPages
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestPatternWebPage {
    @GET("VPATTERNSWEBPAGES?order=SEQNUM")
    fun getDataByPattern(@Query("filter") filter: String): Observable<MPatternWebPages>

    @GET("VPATTERNSWEBPAGES")
    fun getDataById(@Query("filter") filter: String): Observable<MPatternWebPages>

    @FormUrlEncoded
    @PUT("PATTERNSWEBPAGES/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("PATTERNSWEBPAGES/{id}")
    fun update(@Path("id") id: Int, @Field("PATTERNID") patternid: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WEBPAGEID") webpageid: Int): Observable<Int>

    @FormUrlEncoded
    @POST("PATTERNSWEBPAGES")
    fun create(@Field("PATTERNID") patternid: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WEBPAGEID") webpageid: Int): Observable<Int>

    @DELETE("PATTERNSWEBPAGES/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>
}
