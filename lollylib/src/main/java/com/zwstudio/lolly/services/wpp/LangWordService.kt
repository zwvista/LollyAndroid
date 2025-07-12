package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.completeDeleteResult
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.debugCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangWordService {
    private val api = retrofitJson.create(RestLangWord::class.java)
    private val apiSP = retrofitSP.create(RestLangWord::class.java)

    fun getDataByLang(langid: Int): Single<List<MLangWord>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }

    fun updateNote(id: Int, note: String?): Completable =
        api.updateNote(id, note).completeUpdate(id)

    fun update(item: MLangWord): Completable =
        api.update(item.id, item).completeUpdate(item.id)

    fun create(item: MLangWord): Single<Int> =
        api.create(item).debugCreate()

    fun delete(item: MLangWord): Completable =
        apiSP.delete(item.id, item.langid, item.word, item.note, item.famiid, item.correct, item.total)
            .completeDeleteResult()
}
