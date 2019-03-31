package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MDictMean
import com.zwstudio.lolly.domain.MDictNote
import com.zwstudio.lolly.restapi.RestDictMean
import com.zwstudio.lolly.restapi.RestDictNote
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class DictMeanService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MDictMean>> {
        return retrofitJson.create(RestDictMean::class.java)
            .getDataByLang("LANGIDFROM,eq,${langid}")
            .map { it.lst!! }
    }
}

@EBean
class DictNoteService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MDictNote>> {
        return retrofitJson.create(RestDictNote::class.java)
            .getDataByLang("LANGIDFROM,eq,${langid}")
            .map { it.lst!! }
    }
}
