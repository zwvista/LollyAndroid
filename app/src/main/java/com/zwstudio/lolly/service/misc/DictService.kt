package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.domain.misc.MDictionary
import com.zwstudio.lolly.restapi.misc.RestDictNote
import com.zwstudio.lolly.restapi.misc.RestDictReference
import com.zwstudio.lolly.restapi.misc.RestDictTranslation
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class DictionaryService: BaseService() {
    fun getDictsReferenceByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictReference::class.java)
            .getDataByLang("LANGIDFROM,eq,${langid}")
            .map { it.lst!! }

    fun getDictsNoteByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictNote::class.java)
            .getDataByLang("LANGIDFROM,eq,${langid}")
            .map { it.lst!! }

    fun getDictsTranslationByLang(langid: Int): Observable<List<MDictionary>> =
        retrofitJson.create(RestDictTranslation::class.java)
            .getDataByLang("LANGIDFROM,eq,${langid}")
            .map { it.lst!! }
}
