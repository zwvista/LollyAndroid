package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MLangPhrases
import com.zwstudio.lolly.domain.MSPResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestLangPhrase {
    @GET("LANGPHRASES?order=PHRASE")
    fun getDataByLang(@Query("filter") filter: String): Observable<MLangPhrases>

    @GET("LANGPHRASES")
    fun getDataByLangPhrase(@Query("filter") vararg filters: String): Observable<MLangPhrases>

    @GET("LANGPHRASES")
    fun getDataById(@Query("filter") filter: String): Observable<MLangPhrases>

    @FormUrlEncoded
    @PUT("LANGPHRASES/{id}")
    fun updateTranslation(@Path("id") id: Int, @Field("TRANSLATION") translation: String?): Observable<Int>

    @FormUrlEncoded
    @PUT("LANGPHRASES/{id}")
    fun update(@Path("id") id: Int, @Field("LANGID") langid: Int,
               @Field("PHRASE") phrase: String,
               @Field("TRANSLATION") translation: String?): Observable<Int>

    @FormUrlEncoded
    @POST("LANGPHRASES")
    fun create(@Field("LANGID") langid: Int,
               @Field("PHRASE") phrase: String,
               @Field("TRANSLATION") translation: String?): Observable<Int>

    @FormUrlEncoded
    @POST("LANGPHRASES_DELETE")
    fun delete(@Field("P_ID") id: Int, @Field("P_LANGID") langid: Int,
               @Field("P_PHRASE") phrase: String,
               @Field("P_TRANSLATION") translation: String?): Observable<List<List<MSPResult>>>
}
