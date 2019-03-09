package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MUnitPhrases
import io.reactivex.Observable
import retrofit2.http.*

interface RestUnitPhrase {
    @GET("VUNITPHRASES?transform=1&order[]=UNITPART&order[]=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter[]") vararg filters: String): Observable<MUnitPhrases>

    @GET("VUNITPHRASES?transform=1&order[]=TEXTBOOKID&order[]=UNIT&order[]=PART&order[]=SEQNUM")
    fun getDataByLang(@Query("filter") filter: String): Observable<MUnitPhrases>

    @GET("VUNITPHRASES?transform=1")
    fun getDataByLangPhrase(@Query("filter") filter: String): Observable<MUnitPhrases>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun update(@Path("id") id: Int, @Field("TEXTBOOKID") textbookid: Int,
               @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("PHRASEID") phraseid: Int): Observable<Int>

    @FormUrlEncoded
    @POST("VUNITPHRASES")
    fun create(@Field("TEXTBOOKID") textbookid: Int,
               @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("PHRASEID") phraseid: Int): Observable<Int>

    @DELETE("VUNITPHRASES/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
