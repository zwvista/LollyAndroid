package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.DictsNote
import com.zwstudio.lolly.domain.DictsWord
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictWord {
    @GET("VDICTSWORD?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsWord>

}

interface RestDictNote {
    @GET("VDICTSNOTE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsNote>

}
