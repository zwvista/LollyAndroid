package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.models.wpp.MWordsFami
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestWordFami {
    @GET("WORDSFAMI")
    fun getDataByUserWord(@Query("filter") vararg filters: String): Single<MWordsFami>

    @PUT("WORDSFAMI/{id}")
    fun update(@Path("id") id: Int, @Body item: MWordFami): Single<Int>

    @POST("WORDSFAMI")
    fun create(@Body item: MWordFami): Single<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Single<Int>

}
