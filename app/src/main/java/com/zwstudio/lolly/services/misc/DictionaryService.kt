package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.ui.retrofitJson
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import io.reactivex.rxjava3.core.Observable

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
