package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.Languages
import io.reactivex.Observable
import retrofit2.http.GET

interface RestLanguage {
    @GET("LANGUAGES?transform=1&filter=ID,neq,0")
    fun getData(): Observable<Languages>
}
