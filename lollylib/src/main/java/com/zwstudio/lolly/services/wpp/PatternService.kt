package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
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
        api.updateNote(id, note).logUpdate(id)

    fun update(item: MPattern): Completable =
        api.update(item.id, item).logUpdate(item.id)

    fun create(item: MPattern): Single<Int> =
        api.create(item).logCreate()

    fun delete(id: Int): Completable =
        api.delete(id).logDelete()
}
