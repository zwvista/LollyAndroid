package com.zwstudio.lolly.service2.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.rest2api.wpp.Rest2LangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import com.zwstudio.lolly.service.misc.BaseService
import com.zwstudio.lolly.service2.misc.BaseService2
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class LangPhraseService2: BaseService2() {
    suspend fun getDataByLang(langid: Int): List<MLangPhrase> =
        retrofitJson2.create(Rest2LangPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!

    suspend fun updateTranslation(id: Int, translation: String?) =
        retrofitJson2.create(Rest2LangPhrase::class.java)
            .updateTranslation(id, translation)
            .let { Log.d("", it.toString()) }

    suspend fun update(o: MLangPhrase) =
        retrofitJson2.create(Rest2LangPhrase::class.java)
            .update(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("", it.toString()) }

    suspend fun create(o: MLangPhrase): Int =
        retrofitJson2.create(Rest2LangPhrase::class.java)
            .create(o.langid, o.phrase, o.translation)
            .also { Log.d("", it.toString()) }

    suspend fun delete(o: MLangPhrase) =
        retrofitSP2.create(Rest2LangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .let { Log.d("", it.toString()) }
}
