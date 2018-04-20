package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.DictsNote
import com.zwstudio.lolly.domain.DictsOffline
import com.zwstudio.lolly.domain.DictsOnline
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictOnline {
    @GET("VDICTSONLINE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsOnline>

}

interface RestDictOffline {
    @GET("VDICTSOFFLINE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsOffline>

}

interface RestDictNote {
    @GET("VDICTSNOTE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsNote>

}
