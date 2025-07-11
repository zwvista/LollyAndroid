package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogPostContent
import com.zwstudio.lolly.models.blogs.MLangBlogsContent
import retrofit2.http.*

interface RestLangBlogPostContent {
    @GET("LANGBLOGPOSTS")
    suspend fun getDataById(@Query("filter") filter: String): MLangBlogsContent

    @PUT("LANGBLOGPOSTS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangBlogPostContent): Int
}
