package com.zwstudio.lolly.rest2api.wpp

import com.zwstudio.lolly.domain.misc.MSPResult
import com.zwstudio.lolly.domain.wpp.MUnitWords
import retrofit2.http.*

interface Rest2UnitWord {
    @GET("VUNITWORDS?order=UNITPART&order=SEQNUM")
    suspend fun getDataByTextbookUnitPart(@Query("filter") vararg filters: String): MUnitWords

    @GET("VUNITWORDS?order=TEXTBOOKID&order=UNIT&order=PART&order=SEQNUM")
    suspend fun getDataByLang(@Query("filter") filter: String): MUnitWords

    @GET("VUNITWORDS")
    suspend fun getDataByLangWord(@Query("filter") vararg filters: String): MUnitWords

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    suspend fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Int

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Int

    @FormUrlEncoded
    @POST("UNITWORDS_UPDATE")
    suspend fun update(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_WORDID") wordid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): List<List<MSPResult>>

    @FormUrlEncoded
    @POST("UNITWORDS_CREATE")
    suspend fun create(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_WORDID") wordid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): List<List<MSPResult>>

    @FormUrlEncoded
    @POST("UNITWORDS_DELETE")
    suspend fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_WORDID") wordid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): List<List<MSPResult>>

}
