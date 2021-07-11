package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.views.retrofitSP
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class PatternService {
    fun getDataByLang(langid: Int): Single<List<MPattern>> =
        retrofitJson.create(RestPattern::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun getDataById(id: Int): Single<List<MPattern>> =
        retrofitJson.create(RestPattern::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun updateNote(id: Int, note: String): Completable =
        retrofitJson.create(RestPattern::class.java)
            .updateNote(id, note)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun update(o: MPattern): Completable =
        retrofitJson.create(RestPattern::class.java)
            .update(o.id, o.langid, o.pattern, o.note, o.tags)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun create(o: MPattern): Single<Int> =
        retrofitJson.create(RestPattern::class.java)
            .create(o.langid, o.pattern, o.note, o.tags)
            .doAfterSuccess { println(it.toString()) }

    fun delete(id: Int): Completable =
        retrofitJson.create(RestPattern::class.java)
            .delete(id)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun mergePatterns(o: MPattern): Completable =
        retrofitSP.create(RestPattern::class.java)
            .mergePatterns(o.idsMerge, o.pattern, o.note, o.tags)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun splitPattern(o: MPattern): Completable =
        retrofitSP.create(RestPattern::class.java)
            .splitPattern(o.id, o.patternsSplit)
            .flatMapCompletable { println(it.toString()); Completable.complete() }
}
