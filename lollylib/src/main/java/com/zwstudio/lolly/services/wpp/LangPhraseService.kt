package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.models.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import com.zwstudio.lolly.retrofitJson
import com.zwstudio.lolly.retrofitSP

class LangPhraseService {
    fun getDataByLang(langid: Int): Single<List<MLangPhrase>> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun updateTranslation(id: Int, translation: String?): Completable =
        retrofitJson.create(RestLangPhrase::class.java)
            .updateTranslation(id, translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(o: MLangPhrase): Completable =
        retrofitJson.create(RestLangPhrase::class.java)
            .update(o.id, o.langid, o.phrase, o.translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun create(o: MLangPhrase): Single<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .create(o.langid, o.phrase, o.translation)
            .doAfterSuccess { Log.d("API Result", it.toString()) }

    fun delete(o: MLangPhrase): Completable =
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }
}
