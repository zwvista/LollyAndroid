package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.android.retrofitSP
import com.zwstudio.lolly.domain.wpp.MPattern
import com.zwstudio.lolly.restapi.wpp.RestPattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.EBean

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
