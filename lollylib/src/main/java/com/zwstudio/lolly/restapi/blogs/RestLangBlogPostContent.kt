package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogPostContent
import com.zwstudio.lolly.models.blogs.MLangBlogPostContents
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestLangBlogPostContent {
    @GET("LANGBLOGPOSTS")
    suspend fun getDataById(@Query("filter") filter: String): MLangBlogPostContents

    @PUT("LANGBLOGPOSTS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangBlogPostContent): Int
}
