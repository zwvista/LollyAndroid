package com.zwstudio.lolly.rest2api.wpp

import com.zwstudio.lolly.domain.misc.MSPResult
import com.zwstudio.lolly.domain.wpp.MLangWords
import retrofit2.http.*

interface Rest2LangWord {
    @GET("VLANGWORDS?order=WORD")
    suspend fun getDataByLang(@Query("filter") filter: String): MLangWords

    @GET("VLANGWORDS")
    suspend fun getDataByLangWord(@Query("filter") vararg filters: String): MLangWords

    @GET("VLANGWORDS")
    suspend fun getDataById(@Query("filter") filter: String): MLangWords

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Int

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    suspend fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("NOTE") note: String?): Int

    @FormUrlEncoded
    @POST("LANGWORDS")
    suspend fun create(@Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("NOTE") note: String?): Int

    @FormUrlEncoded
    @POST("LANGWORDS_DELETE")
    suspend fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): List<List<MSPResult>>

}
