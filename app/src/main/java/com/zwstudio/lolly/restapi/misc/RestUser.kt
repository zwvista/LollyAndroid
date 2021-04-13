package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MUsers
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface RestUser {
    @GET("USERS")
    fun getData(@Query("filter") vararg filters: String): Observable<MUsers>
}
