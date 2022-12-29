package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import com.zwstudio.lolly.retrofitJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DictionaryService {
    suspend fun getDictsByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestDictionary::class.java)
            .getDictsByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }

    suspend fun getDictsReferenceByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestDictionary::class.java)
            .getDictsReferenceByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }

    suspend fun getDictsNoteByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestDictionary::class.java)
            .getDictsNoteByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }

    suspend fun getDictsTranslationByLang(langid: Int): List<MDictionary> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestDictionary::class.java)
            .getDictsTranslationByLang("LANGIDFROM,eq,$langid")
            .lst!!
    }
}
