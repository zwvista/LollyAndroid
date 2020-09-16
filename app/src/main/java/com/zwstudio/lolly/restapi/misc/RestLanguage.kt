package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MLanguages
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RestLanguage {
    @GET("LANGUAGES?filter=ID,neq,0")
    fun getData(): Observable<MLanguages>
}
