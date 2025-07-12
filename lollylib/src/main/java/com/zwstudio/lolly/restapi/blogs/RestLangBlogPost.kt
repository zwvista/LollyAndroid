package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.models.blogs.MLangBlogPosts
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestLangBlogPost {
    @GET("LANGBLOGPOSTS")
    fun getDataByLang(@Query("filter") filter: String): Single<MLangBlogPosts>

    @PUT("LANGBLOGPOSTS/{id}")
    fun update(@Path("id") id: Int, @Body item: MLangBlogPost): Single<Int>

    @POST("LANGBLOGPOSTS")
    fun create(@Body item: MLangBlogPost): Single<Int>

    @DELETE("LANGBLOGPOSTS/{id}")
    fun delete(@Path("id") id: Int): Single<Int>
}
