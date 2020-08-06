package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MUnitWords
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestUnitWord {
    @GET("VUNITWORDS?order=UNITPART&order=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter") vararg filters: String): Observable<MUnitWords>

    @GET("VUNITWORDS?order=TEXTBOOKID&order=UNIT&order=PART&order=SEQNUM")
    fun getDataByLang(@Query("filter") filter: String): Observable<MUnitWords>

    @GET("VUNITWORDS")
    fun getDataByLangWord(@Query("filter") filter: String): Observable<MUnitWords>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Observable<Int>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun update(@Path("id") id: Int, @Field("TEXTBOOKID") textbookid: Int,
               @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WORDID") wordid: Int): Observable<Int>

    @FormUrlEncoded
    @POST("UNITWORDS")
    fun create(@Field("TEXTBOOKID") textbookid: Int,
               @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WORDID") wordid: Int): Observable<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
