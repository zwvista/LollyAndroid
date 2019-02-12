package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.DictsNote
import com.zwstudio.lolly.domain.DictsMean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestDictMean {
    @GET("VDICTSMEAN?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsMean>

}

interface RestDictNote {
    @GET("VDICTSNOTE?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<DictsNote>

}
