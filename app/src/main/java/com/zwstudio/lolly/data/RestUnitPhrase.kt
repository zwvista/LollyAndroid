package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.UnitPhrases
import io.reactivex.Observable
import retrofit2.http.*

interface RestUnitPhrase {
    @GET("VUNITPHRASES?transform=1&order[]=UNITPART&order[]=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter[]") vararg filters: String): Observable<UnitPhrases>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun updateSeqNum(id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun update(id: Int, @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("PHRASE") phrase: String,
               @Field("TRANSLATION") translation: String): Observable<Int>

    @FormUrlEncoded
    @POST("VUNITPHRASES")
    fun create(@Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("PHRASE") phrase: String,
               @Field("TRANSLATION") translation: String): Observable<Int>

    @DELETE("VUNITPHRASES/{id}")
    fun delete(id: Int): Observable<Int>

}
