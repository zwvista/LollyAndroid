package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MLanguages
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface RestLanguage {
    @GET("LANGUAGES?filter=ID,neq,0")
    fun getData(): Single<MLanguages>
}
