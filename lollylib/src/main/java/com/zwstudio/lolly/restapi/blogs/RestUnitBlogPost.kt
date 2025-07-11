package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MUnitBlogPost
import com.zwstudio.lolly.models.blogs.MUnitBlogPosts
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestUnitBlogPost {
    @GET("UNITBLOGPOSTS")
    suspend fun getDataByTextbook(@Query("filter") vararg filters: String): MUnitBlogPosts

    @PUT("UNITBLOGPOSTS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MUnitBlogPost): Int

    @POST("UNITBLOGPOSTS")
    suspend fun create(@Body item: MUnitBlogPost): Int
}
