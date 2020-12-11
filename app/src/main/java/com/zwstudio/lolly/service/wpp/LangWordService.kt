package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import com.zwstudio.lolly.service.misc.BaseService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class LangWordService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MLangWord>> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun updateNote(id: Int, note: String?): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .updateNote(id, note)
            .map { Log.d("", it.toString()); Unit }

    fun update(o: MLangWord): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .update(o.id, o.langid, o.word, o.note)
            .map { Log.d("", it.toString()); Unit }

    fun create(o: MLangWord): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .create(o.langid, o.word, o.note)
            .doOnNext { Log.d("", it.toString()) }

    fun delete(o: MLangWord): Observable<Unit> =
        retrofitJson.create(RestLangWord::class.java)
            .delete(o.id, o.langid, o.word, o.note, o.famiid, o.correct, o.total)
            .map { Log.d("", it.toString()); Unit }
}
