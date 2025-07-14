package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.logDeleteResult
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangWordService {
    private val api = retrofitJson.create(RestLangWord::class.java)
    private val apiSP = retrofitSP.create(RestLangWord::class.java)

    suspend fun getDataByLang(langid: Int): List<MLangWord> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid").lst
    }

    suspend fun updateNote(id: Int, note: String?) = withContext(Dispatchers.IO) {
        api.updateNote(id, note).logUpdate(id)
    }

    suspend fun update(item: MLangWord) = withContext(Dispatchers.IO) {
        api.update(item.id, item).logUpdate(item.id)
    }

    suspend fun create(item: MLangWord): Int = withContext(Dispatchers.IO) {
        api.create(item).logCreate()
    }

    suspend fun delete(item: MLangWord) = withContext(Dispatchers.IO) {
        apiSP.delete(item.id, item.langid, item.word, item.note, item.famiid, item.correct, item.total)
            .logDeleteResult()
    }
}
