package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import com.zwstudio.lolly.retrofitJson

class LangWordService {
    fun getDataByLang(langid: Int): Single<List<MLangWord>> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }

    fun updateNote(id: Int, note: String?): Completable =
        retrofitJson.create(RestLangWord::class.java)
            .updateNote(id, note)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(o: MLangWord): Completable =
        retrofitJson.create(RestLangWord::class.java)
            .update(o.id, o.langid, o.word, o.note)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun create(o: MLangWord): Single<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .create(o.langid, o.word, o.note)
            .doAfterSuccess { Log.d("API Result", it.toString()) }

    fun delete(o: MLangWord): Completable =
        retrofitJson.create(RestLangWord::class.java)
            .delete(o.id, o.langid, o.word, o.note, o.famiid, o.correct, o.total)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }
}
