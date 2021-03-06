package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MLangWord
import com.zwstudio.lolly.restapi.wpp.RestLangWord
import com.zwstudio.lolly.service.misc.BaseService
import org.androidannotations.annotations.EBean

@EBean
class LangWordService: BaseService() {
    suspend fun getDataByLang(langid: Int): List<MLangWord> =
        retrofitJson2.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!

    suspend fun updateNote(id: Int, note: String?) =
        retrofitJson2.create(RestLangWord::class.java)
            .updateNote(id, note)
            .let { Log.d("", it.toString()) }

    suspend fun update(o: MLangWord) =
        retrofitJson2.create(RestLangWord::class.java)
            .update(o.id, o.langid, o.word, o.note)
            .let { Log.d("", it.toString()) }

    suspend fun create(o: MLangWord): Int =
        retrofitJson2.create(RestLangWord::class.java)
            .create(o.langid, o.word, o.note)
            .also { Log.d("", it.toString()) }

    suspend fun delete(o: MLangWord) =
        retrofitJson2.create(RestLangWord::class.java)
            .delete(o.id, o.langid, o.word, o.note, o.famiid, o.correct, o.total)
            .let { Log.d("", it.toString()) }
}
