package com.zwstudio.lolly.restapi.misc

import com.zwstudio.lolly.models.misc.MUserSettings
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

// https://stackoverflow.com/questions/46892100/how-to-use-rxjava2-with-retrofit-in-android
interface RestUserSetting {
    @GET("USERSETTINGS")
    suspend fun getDataByUser(@Query("filter") filter: String): MUserSettings

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    suspend fun updateValue1(@Path("id") id: Int, @Field("VALUE1") v: String): Int

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    suspend fun updateValue2(@Path("id") id: Int, @Field("VALUE2") v: String): Int

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    suspend fun updateValue3(@Path("id") id: Int, @Field("VALUE3") v: String): Int

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    suspend fun updateValue4(@Path("id") id: Int, @Field("VALUE4") v: String): Int
}
