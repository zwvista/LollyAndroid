package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.LangPhrases
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestLangPhrase {
    @GET("LANGPHRASES?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<LangPhrases>

    @GET("LANGPHRASES?transform=1")
    fun getDataByLangPhrase(@Query("filter[]") vararg filters: String): Observable<LangPhrases>

    @GET("LANGPHRASES?transform=1")
    fun getDataById(@Query("filter") filter: String): Observable<LangPhrases>
}
