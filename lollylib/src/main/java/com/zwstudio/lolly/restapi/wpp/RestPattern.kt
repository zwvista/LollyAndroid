package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.models.wpp.MPatterns
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestPattern {
    @GET("PATTERNS?order=PATTERN")
    suspend fun getDataByLang(@Query("filter") filter: String): MPatterns

    @GET("PATTERNS")
    suspend fun getDataById(@Query("filter") filter: String): MPatterns

    @FormUrlEncoded
    @PUT("PATTERNS/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Field("NOTE") note: String): Int

    @PUT("PATTERNS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MPattern): Int

    @POST("PATTERNS")
    suspend fun create(@Body item: MPattern): Int

    @DELETE("PATTERNS/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}
