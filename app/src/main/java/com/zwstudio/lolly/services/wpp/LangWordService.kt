package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import io.reactivex.rxjava3.core.Observable

class LangWordService {
    fun getDataByLang(langid: Int): Observable<List<MLangWord>> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun updateNote(id: Int, note: String?): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .updateNote(id, note)
            .map { Log.d("API Result", it.toString()); Unit }

    fun update(o: MLangWord): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .update(o.id, o.langid, o.word, o.note)
            .map { Log.d("API Result", it.toString()); Unit }

    fun create(o: MLangWord): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .create(o.langid, o.word, o.note)
            .doOnNext { Log.d("API Result", it.toString()) }

    fun delete(o: MLangWord): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .delete(o.id, o.langid, o.word, o.note, o.famiid, o.correct, o.total)
            .map { Log.d("API Result", it.toString()); Unit }
}
