package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatternService {
    private val api = retrofitJson.create(RestPattern::class.java)

    suspend fun getDataByLang(langid: Int): List<MPattern> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid").lst
    }

    suspend fun getDataById(id: Int): List<MPattern> = withContext(Dispatchers.IO) {
        api.getDataById("ID,eq,$id").lst
    }

    suspend fun updateNote(id: Int, note: String) = withContext(Dispatchers.IO) {
        api.updateNote(id, note).logUpdate(id)
    }

    suspend fun update(item: MPattern) = withContext(Dispatchers.IO) {
        api.update(item.id, item).logUpdate(item.id)
    }

    suspend fun create(item: MPattern): Int = withContext(Dispatchers.IO) {
        api.create(item).logCreate()
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).logDelete()
    }
}
