package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MLangBlogGroup
import com.zwstudio.lolly.models.blogs.MLangBlogGroups
import com.zwstudio.lolly.models.blogs.MLangBlogGPs
import retrofit2.http.*

interface RestLangBlogGroup {
    @GET("LANGBLOGGROUPS")
    suspend fun getDataByLang(@Query("filter") filter: String, @Query("order") order: String): MLangBlogGroups

    @GET("VLANGBLOGGP")
    suspend fun getDataByLangPost(
        @Query("filter") filter1: String,
        @Query("filter") filter2: String,
        @Query("order") order: String
    ): MLangBlogGPs

    @POST("LANGBLOGGROUPS")
    suspend fun create(@Body item: MLangBlogGroup): Int

    @PUT("LANGBLOGGROUPS/{id}")
    suspend fun update(@Path("id") id: Int, @Body item: MLangBlogGroup): Int

    @DELETE("LANGBLOGGROUPS/{id}")
    suspend fun delete(@Path("id") id: Int): Int
}