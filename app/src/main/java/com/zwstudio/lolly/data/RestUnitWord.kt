package com.zwstudio.lolly.data

import com.zwstudio.lolly.domain.UnitWords
import io.reactivex.Observable
import retrofit2.http.*

interface RestUnitWord {
    @GET("VUNITWORDS?transform=1&order[]=UNITPART&order[]=SEQNUM")
    fun getDataByTextbookUnitPart(@Query("filter[]") vararg filters: String): Observable<UnitWords>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun updateSeqNum(id: Int, @Field("SEQNUM") seqnum: Int): Observable<Int>

    @FormUrlEncoded
    @PUT("UNITWORDS/{id}")
    fun update(id: Int, @Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WORD") word: String,
               @Field("NOTE") note: String): Observable<Int>

    @FormUrlEncoded
    @POST("UNITWORDS")
    fun create(@Field("UNIT") unit: Int, @Field("PART") part: Int,
               @Field("SEQNUM") seqnum: Int, @Field("WORD") word: String,
               @Field("NOTE") note: String): Observable<Int>

    @DELETE("UNITWORDS/{id}")
    fun delete(id: Int): Observable<Int>

}