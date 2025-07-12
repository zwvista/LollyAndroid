package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class PatternService {
    private val api = retrofitJson.create(RestPattern::class.java)

    fun getDataByLang(langid: Int): Single<List<MPattern>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }

    fun getDataById(id: Int): Single<List<MPattern>> =
        api.getDataById("ID,eq,$id")
            .map { it.lst }

    fun updateNote(id: Int, note: String): Completable =
        api.updateNote(id, note)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun update(o: MPattern): Completable =
        api.update(o.id, o.langid, o.pattern, o.tags, o.title, o.url)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun create(o: MPattern): Single<Int> =
        api.create(o.langid, o.pattern, o.tags, o.title, o.url)
            .doAfterSuccess { println(it.toString()) }

    fun delete(id: Int): Completable =
        api.delete(id)
            .flatMapCompletable { println(it.toString()); Completable.complete() }
}
