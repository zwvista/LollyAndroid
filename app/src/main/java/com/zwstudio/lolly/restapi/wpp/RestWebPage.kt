package com.zwstudio.lolly.restapi.wpp

import com.zwstudio.lolly.models.wpp.MWebPages
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface RestWebPage {
    @GET("WEBPAGES")
    fun getDataBySearch(@Query("filter") vararg filters: String): Observable<MWebPages>

    @GET("WEBPAGES")
    fun getDataById(@Query("filter") filter: String): Observable<MWebPages>

    @FormUrlEncoded
    @PUT("WEBPAGES/{id}")
    fun update(@Path("id") id: Int,
               @Field("TITLE") title: String, @Field("URL") url: String): Observable<Int>

    @FormUrlEncoded
    @POST("WEBPAGES")
    fun create(@Field("TITLE") title: String, @Field("URL") url: String): Observable<Int>

    @DELETE("WEBPAGES/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>
}
