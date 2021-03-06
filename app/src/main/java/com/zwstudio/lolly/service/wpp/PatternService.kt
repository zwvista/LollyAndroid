package com.zwstudio.lolly.service.wpp

import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import com.zwstudio.lolly.service.misc.BaseService
import org.androidannotations.annotations.EBean

@EBean
class PatternService: BaseService() {
    suspend fun getDataByLang(langid: Int): List<MPattern> =
        retrofitJson2.create(RestPattern::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!

    suspend fun getDataById(id: Int): List<MPattern> =
        retrofitJson2.create(RestPattern::class.java)
            .getDataById("ID,eq,$id")
            .lst!!

    suspend fun updateNote(id: Int, note: String) =
        retrofitJson2.create(RestPattern::class.java)
            .updateNote(id, note)
            .let { println(it.toString()) }

    suspend fun update(o: MPattern) =
        retrofitJson2.create(RestPattern::class.java)
            .update(o.id, o.langid, o.pattern, o.note, o.tags)
            .let { println(it.toString()) }

    suspend fun create(o: MPattern): Int =
        retrofitJson2.create(RestPattern::class.java)
            .create(o.langid, o.pattern, o.note, o.tags)
            .also { println(it.toString()) }

    suspend fun delete(id: Int) =
        retrofitJson2.create(RestPattern::class.java)
            .delete(id)
            .let { println(it.toString()) }

    suspend fun mergePatterns(o: MPattern) =
        retrofitSP2.create(RestPattern::class.java)
            .mergePatterns(o.idsMerge, o.pattern, o.note, o.tags)
            .let { println(it.toString()) }

    suspend fun splitPattern(o: MPattern) =
        retrofitSP2.create(RestPattern::class.java)
            .splitPattern(o.id, o.patternsSplit)
            .let { println(it.toString()) }
}
