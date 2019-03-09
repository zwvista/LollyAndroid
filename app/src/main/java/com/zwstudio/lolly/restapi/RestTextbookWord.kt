package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.MTextbookWords
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbookWord {
    @GET("VTEXTBOOKWORDS?transform=1&order[]=TEXTBOOKID&order[]=UNIT&order[]=PART&order[]=SEQNUM")
    fun getDataByLang(@Query("filter") filter: String): Observable<MTextbookWords>

}
