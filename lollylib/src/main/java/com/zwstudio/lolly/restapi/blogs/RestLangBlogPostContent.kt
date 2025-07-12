package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogPostContent
import com.zwstudio.lolly.models.blogs.MLangBlogsContent
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestLangBlogPostContent {
    @GET("LANGBLOGPOSTS")
    fun getDataById(@Query("filter") filter: String): Single<MLangBlogsContent>

    @PUT("LANGBLOGPOSTS/{id}")
    fun update(@Path("id") id: Int, @Body item: MLangBlogPostContent): Single<Int>
}
