package com.zwstudio.lolly.services.wpp

import android.util.Log
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
        api.updateNote(id, note)
            .let { Log.d("", it.toString()) }
    }

    suspend fun update(o: MPattern) = withContext(Dispatchers.IO) {
        api.update(o.id, o.langid, o.pattern, o.tags, o.title, o.url)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MPattern): Int = withContext(Dispatchers.IO) {
        api.create(o.langid, o.pattern, o.tags, o.title, o.url)
            .also { Log.d("", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id)
            .let { Log.d("", it.toString()) }
    }
}
