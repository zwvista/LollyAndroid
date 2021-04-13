package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MUSMappings
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RestUSMapping {
    @GET("USMAPPINGS")
    fun getData(): Observable<MUSMappings>
}
