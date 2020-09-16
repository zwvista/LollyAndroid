package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MTextbooks
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbook {
    @GET("TEXTBOOKS")
    fun getDataByLang(@Query("filter") filter: String): Observable<MTextbooks>

}
