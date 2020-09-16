package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MDictionaries
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictionary {
    @GET("VDICTIONARIES?order=SEQNUM&order=DICTNAME")
    fun getDictsByLang(@Query("filter") filter: String): Observable<MDictionaries>
    @GET("VDICTSREFERENCE?order=SEQNUM&order=DICTNAME")
    fun getDictsReferenceByLang(@Query("filter") filter: String): Observable<MDictionaries>
    @GET("VDICTSNOTE")
    fun getDictsNoteByLang(@Query("filter") filter: String): Observable<MDictionaries>
    @GET("VDICTSTRANSLATION")
    fun getDictsTranslationByLang(@Query("filter") filter: String): Observable<MDictionaries>
}
