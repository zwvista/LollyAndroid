package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DictionaryService {
    private val api = retrofitJson.create(RestDictionary::class.java)

    suspend fun getDictsByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        api.getDictsByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }

    suspend fun getDictsReferenceByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        api.getDictsReferenceByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }

    suspend fun getDictsNoteByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        api.getDictsNoteByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }

    suspend fun getDictsTranslationByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        api.getDictsTranslationByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }
}
