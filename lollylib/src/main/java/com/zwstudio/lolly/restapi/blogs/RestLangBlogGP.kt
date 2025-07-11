package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogGP
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestLangBlogGP {
    @POST("LANGBLOGGP")
    suspend fun create(@Body item: MLangBlogGP): Int

    @PUT("LANGBLOGGP/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangBlogGP): Int

    @DELETE("LANGBLOGGP/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}