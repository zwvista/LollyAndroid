package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.misc.MSPResult
import com.zwstudio.lolly.models.wpp.MUnitPhrases
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestUnitPhrase {
    @GET("VUNITPHRASES?order=UNITPART&order=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter") vararg filters: String): Single<MUnitPhrases>

    @GET("VUNITPHRASES?order=PHRASEID")
    fun getDataByTextbook(@Query("filter") vararg filters: String): Single<MUnitPhrases>

    @GET("VUNITPHRASES?order=TEXTBOOKID&order=UNIT&order=PART&order=SEQNUM")
    fun getDataByLang(@Query("filter") filter: String): Single<MUnitPhrases>

    @GET("VUNITPHRASES")
    fun getDataByLangPhrase(@Query("filter") vararg filters: String): Single<MUnitPhrases>

    @FormUrlEncoded
    @PUT("VUNITPHRASES/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Single<Int>

    @FormUrlEncoded
    @POST("VUNITPHRASES_UPDATE")
    fun update(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Single<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("VUNITPHRASES_CREATE")
    fun create(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Single<List<List<MSPResult>>>

    @FormUrlEncoded
    @POST("VUNITPHRASES_DELETE")
    fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_TEXTBOOKID") textbookid: Int,
               @Field("P_UNIT") unit: Int, @Field("P_PART") part: Int,
               @Field("P_SEQNUM") seqnum: Int, @Field("P_PHRASEID") phraseid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Single<List<List<MSPResult>>>

}
