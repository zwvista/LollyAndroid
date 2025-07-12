package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangPhraseService {
    private val api = retrofitJson.create(RestLangPhrase::class.java)

    suspend fun getDataByLang(langid: Int): List<MLangPhrase> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid")
            .lst
    }

    suspend fun updateTranslation(id: Int, translation: String?) = withContext(Dispatchers.IO) {
        api.updateTranslation(id, translation)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun update(o: MLangPhrase) = withContext(Dispatchers.IO) {
        api.update(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun create(o: MLangPhrase): Int = withContext(Dispatchers.IO) {
        api.create(o.langid, o.phrase, o.translation)
            .also { Log.d("API Result", it.toString()) }
    }

    suspend fun delete(o: MLangPhrase) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("API Result", it.toString()) }
    }
}
