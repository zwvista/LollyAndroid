package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MDictNote
import com.zwstudio.lolly.domain.MDictReference
import com.zwstudio.lolly.domain.MDictTranslation
import com.zwstudio.lolly.restapi.RestDictNote
import com.zwstudio.lolly.restapi.RestDictReference
import com.zwstudio.lolly.restapi.RestDictTranslation
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class DictReferenceService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MDictReference>> {
        return retrofitJson.create(RestDictReference::class.java)
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

@EBean
class DictTranslationService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MDictTranslation>> {
        return retrofitJson.create(RestDictTranslation::class.java)
            .getDataByLang("LANGIDFROM,eq,${langid}")
            .map { it.lst!! }
    }
}
