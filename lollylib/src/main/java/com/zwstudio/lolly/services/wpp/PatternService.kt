package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import com.zwstudio.lolly.services.misc.retrofitJson
import com.zwstudio.lolly.services.misc.retrofitSP
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
            .update(o.id, o.langid, o.pattern, o.note, o.tags)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MPattern): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .create(o.langid, o.pattern, o.note, o.tags)
            .also { Log.d("", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPattern::class.java)
            .delete(id)
            .let { Log.d("", it.toString()) }
    }

    suspend fun mergePatterns(o: MPattern) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestPattern::class.java)
            .mergePatterns(o.idsMerge, o.pattern, o.note, o.tags)
            .let { Log.d("", it.toString()) }
    }

    suspend fun splitPattern(o: MPattern) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestPattern::class.java)
            .splitPattern(o.id, o.patternsSplit)
            .let { Log.d("", it.toString()) }
    }
}
