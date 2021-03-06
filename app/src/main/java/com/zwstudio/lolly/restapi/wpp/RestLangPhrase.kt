package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.domain.misc.MSPResult
import com.zwstudio.lolly.domain.wpp.MLangPhrases
import retrofit2.http.*

interface RestLangPhrase {
    @GET("LANGPHRASES?order=PHRASE")
    suspend fun getDataByLang(@Query("filter") filter: String): MLangPhrases

    @GET("LANGPHRASES")
    suspend fun getDataByLangPhrase(@Query("filter") vararg filters: String): MLangPhrases

    @GET("LANGPHRASES")
    suspend fun getDataById(@Query("filter") filter: String): MLangPhrases

    @FormUrlEncoded
    @PUT("LANGPHRASES/{id}")
    suspend fun updateTranslation(@Path("id") id: Int, @Field("TRANSLATION") translation: String?): Int

    @FormUrlEncoded
    @PUT("LANGPHRASES/{id}")
    suspend fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("PHRASE") phrase: String,
               @Field("TRANSLATION") translation: String?): Int

    @FormUrlEncoded
    @POST("LANGPHRASES")
    suspend fun create(@Field("LANGID") langid: Int,
               @Field("PHRASE") phrase: String,
               @Field("TRANSLATION") translation: String?): Int

    @FormUrlEncoded
    @POST("LANGPHRASES_DELETE")
    suspend fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): List<List<MSPResult>>
}
