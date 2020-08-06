package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MUSMappings
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RestUSMapping {
    @GET("USMAPPINGS")
    fun getData(): Observable<MUSMappings>
}
