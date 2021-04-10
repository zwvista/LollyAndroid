package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.domain.misc.MUsers
import retrofit2.http.GET
import retrofit2.http.Query


interface RestUser {
    @GET("USERS")
    suspend fun getData(@Query("filter") vararg filters: String): MUsers
}
