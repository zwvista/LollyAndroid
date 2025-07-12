package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import io.reactivex.rxjava3.core.Single

class DictionaryService {
    private val api = retrofitJson.create(RestDictionary::class.java)

    fun getDictsByLang(langid: Int): Single<List<MDictionary>> =
        api.getDictsByLang("LANGIDFROM,eq,$langid")
            .map { it.lst }

    fun getDictsReferenceByLang(langid: Int): Single<List<MDictionary>> =
        api.getDictsReferenceByLang("LANGIDFROM,eq,$langid")
            .map { it.lst }

    fun getDictsNoteByLang(langid: Int): Single<List<MDictionary>> =
        api.getDictsNoteByLang("LANGIDFROM,eq,$langid")
            .map { it.lst }

    fun getDictsTranslationByLang(langid: Int): Single<List<MDictionary>> =
        api.getDictsTranslationByLang("LANGIDFROM,eq,$langid")
            .map { it.lst }
}
