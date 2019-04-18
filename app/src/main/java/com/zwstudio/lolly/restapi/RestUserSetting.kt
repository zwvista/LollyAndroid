package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MUserSettings
import io.reactivex.Observable
import retrofit2.http.*

// https://stackoverflow.com/questions/46892100/how-to-use-rxjava2-with-retrofit-in-android
interface RestUserSetting {
    @GET("USERSETTINGS?transform=1")
    fun getDataByUser(@Query("filter") filter: String): Observable<MUserSettings>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateLang(@Path("id") id: Int, @Field("VALUE1") langid: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateTextbook(@Path("id") id: Int, @Field("VALUE1") textbookid: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateDictItem(@Path("id") id: Int, @Field("VALUE2") dictitem: String): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateDictNote(@Path("id") id: Int, @Field("VALUE3") dictnodeid: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateDictTranslation(@Path("id") id: Int, @Field("VALUE1") dictnodeid: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateUnitFrom(@Path("id") id: Int, @Field("VALUE1") unitfrom: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updatePartFrom(@Path("id") id: Int, @Field("VALUE2") partfrom: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateUnitTo(@Path("id") id: Int, @Field("VALUE3") unitto: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updatePartTo(@Path("id") id: Int, @Field("VALUE4") partto: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("USERSETTINGS/{id}")
    fun updateVoice(@Path("id") id: Int, @Field("VALUE4") voiceid: Int): Observable<Int>
}