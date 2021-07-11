package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import com.zwstudio.lolly.views.retrofitJson
import io.reactivex.rxjava3.core.Single

class DictionaryService {
    fun getDictsByLang(langid: Int): Single<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }

    fun getDictsReferenceByLang(langid: Int): Single<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsReferenceByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }

    fun getDictsNoteByLang(langid: Int): Single<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsNoteByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }

    fun getDictsTranslationByLang(langid: Int): Single<List<MDictionary>> =
        retrofitJson.create(RestDictionary::class.java)
            .getDictsTranslationByLang("LANGIDFROM,eq,$langid")
            .map { it.lst!! }
}
