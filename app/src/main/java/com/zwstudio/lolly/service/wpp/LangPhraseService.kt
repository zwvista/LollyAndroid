package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import com.zwstudio.lolly.service.misc.BaseService
import org.androidannotations.annotations.EBean

@EBean
class LangPhraseService: BaseService() {
    suspend fun getDataByLang(langid: Int): List<MLangPhrase> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!

    suspend fun updateTranslation(id: Int, translation: String?) =
        retrofitJson.create(RestLangPhrase::class.java)
            .updateTranslation(id, translation)
            .let { Log.d("", it.toString()) }

    suspend fun update(o: MLangPhrase) =
        retrofitJson.create(RestLangPhrase::class.java)
            .update(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("", it.toString()) }

    suspend fun create(o: MLangPhrase): Int =
        retrofitJson.create(RestLangPhrase::class.java)
            .create(o.langid, o.phrase, o.translation)
            .also { Log.d("", it.toString()) }

    suspend fun delete(o: MLangPhrase) =
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("", it.toString()) }
}
