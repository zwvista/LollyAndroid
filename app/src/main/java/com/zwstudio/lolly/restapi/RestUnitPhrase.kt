package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.UnitPhrases
import io.reactivex.Observable
import retrofit2.http.*

interface RestUnitPhrase {
    @GET("VUNITPHRASES?transform=1&order[]=UNITPART&order[]=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter[]") vararg filters: String): Observable<UnitPhrases>

    @GET("VUNITPHRASES?transform=1")
    fun getDataByLangPhrase(@Query("filter") filter: String): Observable<UnitPhrases>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun update(@Path("id") id: Int, @Field("TEXTBOOKID") textbookid: Int,
               @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("LANGPHRASEID") langphraseid: Int): Observable<Int>

    @FormUrlEncoded
    @POST("VUNITPHRASES")
    fun create(@Field("TEXTBOOKID") textbookid: Int,
               @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("LANGPHRASEID") langphraseid: Int): Observable<Int>

    @DELETE("VUNITPHRASES/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
