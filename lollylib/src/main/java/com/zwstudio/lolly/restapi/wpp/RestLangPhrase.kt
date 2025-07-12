package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.misc.MSPResult
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.models.wpp.MLangPhrases
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @PUT("LANGPHRASES/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangPhrase): Int

    @POST("LANGPHRASES")
    suspend fun create(@Body item: MLangPhrase): Int

    @FormUrlEncoded
    @POST("LANGPHRASES_DELETE")
    suspend fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): List<List<MSPResult>>
}
