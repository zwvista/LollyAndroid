package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.ui.retrofitJson
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangWordService {
    suspend fun getDataByLang(langid: Int): List<MLangWord> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
    }

    suspend fun updateNote(id: Int, note: String?) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangWord::class.java)
            .updateNote(id, note)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun update(o: MLangWord) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangWord::class.java)
            .update(o.id, o.langid, o.word, o.note)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun create(o: MLangWord): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangWord::class.java)
            .create(o.langid, o.word, o.note)
            .also { Log.d("API Result", it.toString()) }
    }

    suspend fun delete(o: MLangWord) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangWord::class.java)
            .delete(o.id, o.langid, o.word, o.note, o.famiid, o.correct, o.total)
            .let { Log.d("API Result", it.toString()) }
    }
}
