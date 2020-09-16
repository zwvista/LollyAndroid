package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.misc.MSPResult
import com.zwstudio.lolly.domain.wpp.MUnitWords
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestUnitWord {
    @GET("VUNITWORDS?order=UNITPART&order=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter") vararg filters: String): Observable<MUnitWords>

    @GET("VUNITWORDS?order=TEXTBOOKID&order=UNIT&order=PART&order=SEQNUM")
    fun getDataByLang(@Query("filter") filter: String): Observable<MUnitWords>

    @GET("VUNITWORDS")
    fun getDataByLangWord(@Query("filter") vararg filters: String): Observable<MUnitWords>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Observable<Int>

    @FormUrlEncoded
    @POST("UNITWORDS_UPDATE")
    fun update(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_WORDID") wordid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): Observable<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("UNITWORDS_CREATE")
    fun create(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_WORDID") wordid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): Observable<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("UNITWORDS_DELETE")
    fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_WORDID") wordid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): Observable<List<List<MSPResult>>>

}
