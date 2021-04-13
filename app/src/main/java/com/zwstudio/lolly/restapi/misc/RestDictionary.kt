package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MDictionaries
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictionary {
    @GET("VDICTIONARIES?order=SEQNUM&order=DICTNAME")
    suspend fun getDictsByLang(@Query("filter") filter: String): MDictionaries
    @GET("VDICTSREFERENCE?order=SEQNUM&order=DICTNAME")
    suspend fun getDictsReferenceByLang(@Query("filter") filter: String): MDictionaries
    @GET("VDICTSNOTE")
    suspend fun getDictsNoteByLang(@Query("filter") filter: String): MDictionaries
    @GET("VDICTSTRANSLATION")
    suspend fun getDictsTranslationByLang(@Query("filter") filter: String): MDictionaries
}
