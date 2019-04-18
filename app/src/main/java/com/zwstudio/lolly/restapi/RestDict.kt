package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MDictsNote
import com.zwstudio.lolly.domain.MDictsReference
import com.zwstudio.lolly.domain.MDictsTranslation
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictReference {
    @GET("VDICTSREFERENCE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictsReference>

}

interface RestDictNote {
    @GET("VDICTSNOTE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictsNote>

}

interface RestDictTranslation {
    @GET("VDICTSTRANSLATION?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<MDictsTranslation>

}
