package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.android.retrofitSP
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangPhraseService {
    suspend fun getDataByLang(langid: Int): List<MLangPhrase> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
    }

    suspend fun updateTranslation(id: Int, translation: String?) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangPhrase::class.java)
            .updateTranslation(id, translation)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun update(o: MLangPhrase) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangPhrase::class.java)
            .update(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun create(o: MLangPhrase): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLangPhrase::class.java)
            .create(o.langid, o.phrase, o.translation)
            .also { Log.d("API Result", it.toString()) }
    }

    suspend fun delete(o: MLangPhrase) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("API Result", it.toString()) }
    }
}
