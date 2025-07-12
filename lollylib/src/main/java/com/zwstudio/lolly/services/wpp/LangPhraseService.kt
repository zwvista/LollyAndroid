package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.completeDeleteResult
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.debugCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangPhraseService {
    private val api = retrofitJson.create(RestLangPhrase::class.java)
    private val apiSP = retrofitSP.create(RestLangPhrase::class.java)

    fun getDataByLang(langid: Int): Single<List<MLangPhrase>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }

    fun updateTranslation(id: Int, translation: String?): Completable =
        api.updateTranslation(id, translation).completeUpdate(id)

    fun update(item: MLangPhrase): Completable =
        api.update(item.id, item).completeUpdate(item.id)

    fun create(item: MLangPhrase): Single<Int> =
        api.create(item).debugCreate()

    fun delete(item: MLangPhrase): Completable =
        apiSP.delete(item.id, item.langid, item.phrase, item.translation).completeDeleteResult()
}
