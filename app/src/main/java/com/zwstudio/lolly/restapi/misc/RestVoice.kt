package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MVoices
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestVoice {
    @GET("VVOICES")
    fun getDataByLang(@Query("filter") vararg filters: String): Observable<MVoices>
}
