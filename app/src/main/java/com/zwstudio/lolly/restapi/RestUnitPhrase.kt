package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MSPResult
import com.zwstudio.lolly.domain.MUnitPhrases
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestUnitPhrase {
    @GET("VUNITPHRASES?order=UNITPART&order=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter") vararg filters: String): Observable<MUnitPhrases>

    @GET("VUNITPHRASES?order=TEXTBOOKID&order=UNIT&order=PART&order=SEQNUM")
    fun getDataByLang(@Query("filter") filter: String): Observable<MUnitPhrases>

    @GET("VUNITPHRASES")
    fun getDataByLangPhrase(@Query("filter") filter: String): Observable<MUnitPhrases>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @POST("VUNITPHRASES_UPDATE")
    fun update(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Observable<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("VUNITPHRASES_CREATE")
    fun create(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Observable<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("VUNITPHRASES_DELETE")
    fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Observable<List<List<MSPResult>>>

}
