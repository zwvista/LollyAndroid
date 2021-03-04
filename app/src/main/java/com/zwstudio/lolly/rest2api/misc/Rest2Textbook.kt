package com.zwstudio.suspendapi.restapi.misc

import com.zwstudio.lolly.domain.misc.MTextbooks
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Rest2Textbook {
    @GET("TEXTBOOKS")
    suspend fun getDataByLang(@Query("filter") filter: String): MTextbooks

}
