package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

class DictionaryService {
    fun getDictsByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }

    fun getDictsReferenceByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsReferenceByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }

    fun getDictsNoteByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsNoteByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }

    fun getDictsTranslationByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsTranslationByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }
}
