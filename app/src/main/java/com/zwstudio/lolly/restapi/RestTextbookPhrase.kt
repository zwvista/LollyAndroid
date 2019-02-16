package com.zwstudio.lolly.restapi

import com.zwstudio.lolly.domain.TextbookPhrase
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestTextbookPhrase {
    @GET("VTEXTBOOKPHRASES?transform=1")
    fun getDataByLang(@Query("filter") filter: String): Observable<TextbookPhrase>

}
