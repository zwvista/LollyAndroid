package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.models.blogs.MLangBlogPosts
import retrofit2.http.*

interface RestLangBlogPost {
    @GET("LANGBLOGPOSTS")
    suspend fun getDataByLang(@Query("filter") filter: String): MLangBlogPosts

    @PUT("LANGBLOGPOSTS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangBlogPost): Int

    @POST("LANGBLOGPOSTS")
    suspend fun create(@Body item: MLangBlogPost): Int

    @DELETE("LANGBLOGPOSTS/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}
