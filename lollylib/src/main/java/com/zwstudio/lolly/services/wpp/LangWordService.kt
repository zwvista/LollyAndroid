package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangWordService {
    private val api = retrofitJson.create(RestLangWord::class.java)

    fun getDataByLang(langid: Int): Single<List<MLangWord>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }

    fun updateNote(id: Int, note: String?): Completable =
        api.updateNote(id, note)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(o: MLangWord): Completable =
        api.update(o.id, o.langid, o.word, o.note)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun create(o: MLangWord): Single<Int> =
        api.create(o.langid, o.word, o.note)
            .doAfterSuccess { Log.d("API Result", it.toString()) }

    fun delete(o: MLangWord): Completable =
        api.delete(o.id, o.langid, o.word, o.note, o.famiid, o.correct, o.total)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }
}
