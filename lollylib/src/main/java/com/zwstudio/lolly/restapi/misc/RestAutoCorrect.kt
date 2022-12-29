package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MAutoCorrects
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RestAutoCorrect {
    @GET("AUTOCORRECT")
    fun getDataByLang(@Query("filter") filter: String): Single<MAutoCorrects>

}
