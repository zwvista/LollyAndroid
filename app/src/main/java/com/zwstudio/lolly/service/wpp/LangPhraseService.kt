package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.android.retrofitSP
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import io.reactivex.rxjava3.core.Observable

class LangPhraseService {
    fun getDataByLang(langid: Int): Observable<List<MLangPhrase>> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun updateTranslation(id: Int, translation: String?): Observable<Unit> =
        retrofitJson.create(RestLangPhrase::class.java)
            .updateTranslation(id, translation)
            .map { Log.d("", it.toString()); Unit }

    fun update(o: MLangPhrase): Observable<Unit> =
        retrofitJson.create(RestLangPhrase::class.java)
            .update(o.id, o.langid, o.phrase, o.translation)
            .map { Log.d("", it.toString()); Unit }

    fun create(o: MLangPhrase): Observable<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .create(o.langid, o.phrase, o.translation)
            .doOnNext { Log.d("", it.toString()) }

    fun delete(o: MLangPhrase): Observable<Unit> =
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .map { Log.d("", it.toString()); Unit }
}
