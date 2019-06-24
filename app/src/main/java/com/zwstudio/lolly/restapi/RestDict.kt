package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MDictsNote
import com.zwstudio.lolly.domain.MDictsReference
import com.zwstudio.lolly.domain.MDictsTranslation
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictReference {
    @GET("VDICTSREFERENCE?order=SEQNUM&order=DICTNAME")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictsReference>

}

interface RestDictNote {
    @GET("VDICTSNOTE")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictsNote>

}

interface RestDictTranslation {
    @GET("VDICTSTRANSLATION")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictsTranslation>

}
