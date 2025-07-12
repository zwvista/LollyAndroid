package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.misc.MSPResult
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.models.wpp.MLangWords
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestLangWord {
    @GET("VLANGWORDS?order=WORD")
    suspend fun getDataByLang(@Query("filter") filter: String): MLangWords

    @GET("VLANGWORDS")
    suspend fun getDataByLangWord(@Query("filter") vararg filters: String): MLangWords

    @GET("VLANGWORDS")
    suspend fun getDataById(@Query("filter") filter: String): MLangWords

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Int

    @PUT("LANGWORDS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangWord): Int

    @POST("LANGWORDS")
    suspend fun create(@Body item: MLangWord): Int

    @FormUrlEncoded
    @POST("LANGWORDS_DELETE")
    suspend fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): List<List<MSPResult>>

}
