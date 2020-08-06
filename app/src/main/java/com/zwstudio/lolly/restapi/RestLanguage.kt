package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MLanguages
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RestLanguage {
    @GET("LANGUAGES?filter=ID,neq,0")
    fun getData(): Observable<MLanguages>
}
