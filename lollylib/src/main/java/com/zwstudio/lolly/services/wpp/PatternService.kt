package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatternService {
    suspend fun getDataByLang(langid: Int): List<MPattern> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
    }

    suspend fun getDataById(id: Int): List<MPattern> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .getDataById("ID,eq,$id")
            .lst!!
    }

    suspend fun updateNote(id: Int, note: String) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .updateNote(id, note)
            .let { Log.d("", it.toString()) }
    }

    suspend fun update(o: MPattern) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .update(o.id, o.langid, o.pattern, o.tags, o.title, o.url)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MPattern): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .create(o.langid, o.pattern, o.tags, o.title, o.url)
            .also { Log.d("", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .delete(id)
            .let { Log.d("", it.toString()) }
    }
}
