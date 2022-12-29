package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.misc.MSPResult
import com.zwstudio.lolly.models.wpp.MLangWords
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

interface RestLangWord {
    @GET("VLANGWORDS?order=WORD")
    fun getDataByLang(@Query("filter") filter: String): Single<MLangWords>

    @GET("VLANGWORDS")
    fun getDataByLangWord(@Query("filter") vararg filters: String): Single<MLangWords>

    @GET("VLANGWORDS")
    fun getDataById(@Query("filter") filter: String): Single<MLangWords>

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String?): Single<Int>

    @FormUrlEncoded
    @PUT("LANGWORDS/{id}")
    fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("NOTE") note: String?): Single<Int>

    @FormUrlEncoded
    @POST("LANGWORDS")
    fun create(@Field("LANGID") langid: Int,
               @Field("WORD") word: String, @Field("NOTE") note: String?): Single<Int>

    @FormUrlEncoded
    @POST("LANGWORDS_DELETE")
    fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_WORD") word: String, @Field("P_NOTE") note: String?,
               @Field("P_FAMIID") famiid: Int,
               @Field("P_CORRECT") correct: Int, @Field("P_TOTAL") total: Int): Single<List<List<MSPResult>>>

}
