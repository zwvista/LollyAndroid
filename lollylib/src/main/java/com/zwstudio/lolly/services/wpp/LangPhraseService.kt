package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.logDeleteResult
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangPhraseService {
    private val api = retrofitJson.create(RestLangPhrase::class.java)
    private val apiSP = retrofitSP.create(RestLangPhrase::class.java)

    suspend fun getDataByLang(langid: Int): List<MLangPhrase> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid").lst
    }

    suspend fun updateTranslation(id: Int, translation: String?) = withContext(Dispatchers.IO) {
        api.updateTranslation(id, translation).logUpdate(id)
    }

    suspend fun update(item: MLangPhrase) = withContext(Dispatchers.IO) {
        api.update(item.id, item).logUpdate(item.id)
    }

    suspend fun create(item: MLangPhrase): Int = withContext(Dispatchers.IO) {
        api.create(item).logCreate()
    }

    suspend fun delete(item: MLangPhrase): Unit = withContext(Dispatchers.IO) {
        apiSP.delete(item.id, item.langid, item.phrase, item.translation).logDeleteResult()
    }
}
