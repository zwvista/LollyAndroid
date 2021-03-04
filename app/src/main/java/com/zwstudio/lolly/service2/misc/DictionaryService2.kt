package com.zwstudio.lolly.service2.misc

import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.rest2api.misc.Rest2Dictionary
import com.zwstudio.lolly.restapi.misc.RestDictionary
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class DictionaryService2: BaseService2() {
    suspend fun getDictsByLang(langid: Int): List<MDictionary> =
        retrofitJson2.create(Rest2Dictionary::class.java)
            .getDictsByLang("LANGIDFROM,eq,$langid")
            .lst!!

    suspend fun getDictsReferenceByLang(langid: Int): List<MDictionary> =
        retrofitJson2.create(Rest2Dictionary::class.java)
            .getDictsReferenceByLang("LANGIDFROM,eq,$langid")
            .lst!!

    suspend fun getDictsNoteByLang(langid: Int): List<MDictionary> =
        retrofitJson2.create(Rest2Dictionary::class.java)
            .getDictsNoteByLang("LANGIDFROM,eq,$langid")
            .lst!!

    suspend fun getDictsTranslationByLang(langid: Int): List<MDictionary> =
        retrofitJson2.create(Rest2Dictionary::class.java)
            .getDictsTranslationByLang("LANGIDFROM,eq,$langid")
            .lst!!
}
