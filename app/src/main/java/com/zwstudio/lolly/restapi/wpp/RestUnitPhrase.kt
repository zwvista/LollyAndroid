package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.misc.MSPResult
import com.zwstudio.lolly.domain.wpp.MUnitPhrases
import retrofit2.http.*

interface RestUnitPhrase {
    @GET("VUNITPHRASES?order=UNITPART&order=SEQNUM")
    suspend fun getDataByTextbookUnitPart(@Query("filter") vararg filters: String): MUnitPhrases

    @GET("VUNITPHRASES?order=TEXTBOOKID&order=UNIT&order=PART&order=SEQNUM")
    suspend fun getDataByLang(@Query("filter") filter: String): MUnitPhrases

    @GET("VUNITPHRASES")
    suspend fun getDataByLangPhrase(@Query("filter") vararg filters: String): MUnitPhrases

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    suspend fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Int

    @FormUrlEncoded
    @POST("VUNITPHRASES_UPDATE")
    suspend fun update(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): List<List<MSPResult>>

    @FormUrlEncoded
    @POST("VUNITPHRASES_CREATE")
    suspend fun create(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): List<List<MSPResult>>

    @FormUrlEncoded
    @POST("VUNITPHRASES_DELETE")
    suspend fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): List<List<MSPResult>>

}
