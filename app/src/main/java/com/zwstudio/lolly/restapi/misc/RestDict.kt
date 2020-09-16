package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MDictionaries
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictReference {
    @GET("VDICTSREFERENCE?order=SEQNUM&order=DICTNAME")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictionaries>

}

interface RestDictNote {
    @GET("VDICTSNOTE")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictionaries>

}

interface RestDictTranslation {
    @GET("VDICTSTRANSLATION")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictionaries>

}
