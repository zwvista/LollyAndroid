package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogGPs
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.models.blogs.MLangBlogPosts
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestLangBlogPost {
    @GET("LANGBLOGPOSTS")
    suspend fun getDataByLang(@Query("filter") filter: String): MLangBlogPosts

    @GET("VLANGBLOGGP?order=TITLE")
    suspend fun getDataByLangGroup(@Query("filter") vararg filters: String): MLangBlogGPs

    @PUT("LANGBLOGPOSTS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangBlogPost): Int

    @POST("LANGBLOGPOSTS")
    suspend fun create(@Body item: MLangBlogPost): Int

    @DELETE("LANGBLOGPOSTS/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}
