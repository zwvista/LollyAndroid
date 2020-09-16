package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MAutoCorrects
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAutoCorrect {
    @GET("AUTOCORRECT")
    fun getDataByLang(@Query("filter") filter: String): Observable<MAutoCorrects>

}
