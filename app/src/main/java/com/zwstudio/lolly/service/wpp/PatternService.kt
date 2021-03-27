package com.zwstudio.lolly.service.wpp

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.android.retrofitSP
import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class PatternService {
    fun getDataByLang(langid: Int): Observable<List<MPattern>> =
        retrofitJson.create(RestPattern::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun getDataById(id: Int): Observable<List<MPattern>> =
        retrofitJson.create(RestPattern::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun updateNote(id: Int, note: String): Observable<Unit> =
        retrofitJson.create(RestPattern::class.java)
            .updateNote(id, note)
            .map { println(it.toString()) }

    fun update(o: MPattern): Observable<Unit> =
        retrofitJson.create(RestPattern::class.java)
            .update(o.id, o.langid, o.pattern, o.note, o.tags)
            .map { println(it.toString()) }

    fun create(o: MPattern): Observable<Int> =
        retrofitJson.create(RestPattern::class.java)
            .create(o.langid, o.pattern, o.note, o.tags)
            .doOnNext { println(it.toString()) }

    fun delete(id: Int): Observable<Unit> =
        retrofitJson.create(RestPattern::class.java)
            .delete(id)
            .map { println(it.toString()) }

    fun mergePatterns(o: MPattern): Observable<Unit> =
        retrofitSP.create(RestPattern::class.java)
            .mergePatterns(o.idsMerge, o.pattern, o.note, o.tags)
            .map { println(it.toString()) }

    fun splitPattern(o: MPattern): Observable<Unit> =
        retrofitSP.create(RestPattern::class.java)
            .splitPattern(o.id, o.patternsSplit)
            .map { println(it.toString()) }
}
