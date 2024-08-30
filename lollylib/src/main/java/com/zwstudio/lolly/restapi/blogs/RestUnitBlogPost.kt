package com.zwstudio.lolly.restapi.blogs

import com.zwstudio.lolly.models.blogs.MUnitBlogPosts
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RestUnitBlogPost {
    @GET("UNITBLOGPOSTS")
    suspend fun getDataByTextbook(@Query("filter") vararg filters: String): MUnitBlogPosts

    @FormUrlEncoded
    @PUT("UNITBLOGPOSTS/{id}")
    suspend fun update(@Path("id") id: Int, @Field("TEXTBOOKID") textbookid: Int,
                       @Field("UNIT") unit: Int,
                       @Field("CONTENT") content: String): Int

    @FormUrlEncoded
    @POST("UNITBLOGPOSTS")
    suspend fun create(@Field("TEXTBOOKID") textbookid: Int,
                       @Field("UNIT") unit: Int,
                       @Field("CONTENT") content: String): Int
}
