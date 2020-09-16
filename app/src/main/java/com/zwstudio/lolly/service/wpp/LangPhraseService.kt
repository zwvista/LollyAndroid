package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MLangPhrase
import com.zwstudio.lolly.restapi.wpp.RestLangPhrase
import com.zwstudio.lolly.service.misc.BaseService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class LangPhraseService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MLangPhrase>> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,${langid}")
            .map { it.lst!! }

    fun updateTranslation(id: Int, translation: String?): Observable<Unit> =
        retrofitJson.create(RestLangPhrase::class.java)
            .updateTranslation(id, translation)
            .map { Log.d("", it.toString()); Unit }

    fun update(id: Int, langid: Int, phrase: String, translation: String?): Observable<Unit> =
        retrofitJson.create(RestLangPhrase::class.java)
            .update(id, langid, phrase, translation)
            .map { Log.d("", it.toString()); Unit }

    fun create(langid: Int, phrase: String, translation: String?): Observable<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .create(langid, phrase, translation)
            .doOnNext { Log.d("", it.toString()) }

    fun delete(o: MLangPhrase): Observable<Unit> =
        retrofitSP.create(RestLangPhrase::class.java)
            .delete(o.id, o.langid, o.phrase, o.translation)
            .map { Log.d("", it.toString()); Unit }
}
