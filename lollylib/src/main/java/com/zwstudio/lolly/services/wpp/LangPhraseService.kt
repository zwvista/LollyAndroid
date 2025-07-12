package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangPhraseService {
    private val api = retrofitJson.create(RestLangPhrase::class.java)

    fun getDataByLang(langid: Int): Single<List<MLangPhrase>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }

    fun updateTranslation(id: Int, translation: String?): Completable =
        api.updateTranslation(id, translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(o: MLangPhrase): Completable =
        api.update(o.id, o.langid, o.phrase, o.translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun create(o: MLangPhrase): Single<Int> =
        api.create(o.langid, o.phrase, o.translation)
            .doAfterSuccess { Log.d("API Result", it.toString()) }

    fun delete(o: MLangPhrase): Completable =
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }
}
