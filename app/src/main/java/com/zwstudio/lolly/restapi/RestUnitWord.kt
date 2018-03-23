package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.UnitWords
import io.reactivex.Observable
import retrofit2.http.*

interface RestUnitWord {
    @GET("VUNITWORDS?transform=1&order[]=UNITPART&order[]=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter[]") vararg filters: String): Observable<UnitWords>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun updateSeqNum(@Path("id") id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun update(@Path("id") id: Int, @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WORD") word: String,
               @Field("NOTE") note: String): Observable<Int>

    @FormUrlEncoded
    @POST("UNITWORDS")
    fun create(@Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WORD") word: String,
               @Field("NOTE") note: String): Observable<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(@Path("id") id: Int): Observable<Int>

}
