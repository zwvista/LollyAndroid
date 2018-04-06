package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.NoteSites
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestNoteSite {
    @GET("VNOTESITES?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<NoteSites>

}
