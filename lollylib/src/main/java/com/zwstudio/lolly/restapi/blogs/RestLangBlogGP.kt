package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogGP
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RestLangBlogGP {
    @POST("LANGBLOGGP")
    fun create(@Body item: MLangBlogGP): Single<Int>

    @PUT("LANGBLOGGP/{id}")
    fun update(@Path("id") id: Int, @Body item: MLangBlogGP): Single<Int>

    @DELETE("LANGBLOGGP/{id}")
    fun delete(@Path("id") id: Int): Single<Int>
}