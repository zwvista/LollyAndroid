package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MUSMappings
import io.reactivex.Observable
import retrofit2.http.GET

interface RestUSMapping {
    @GET("USMAPPINGS")
    fun getData(): Observable<MUSMappings>
}
