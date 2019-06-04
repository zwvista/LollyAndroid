package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MLangPhrases
import io.reactivex.Observable
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

    @DELETE("LANGPHRASES/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>
}
