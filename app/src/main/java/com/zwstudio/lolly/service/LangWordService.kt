package com.zwstudio.lolly.service

import android.util.Log
import com.zwstudio.lolly.domain.MLangWord
import com.zwstudio.lolly.domain.MTextbook
import com.zwstudio.lolly.restapi.RestLangWord
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean
import java.net.URLEncoder

@EBean
class LangWordService: BaseService() {
    fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): Observable<List<MLangWord>> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLang("LANGID,eq,${langid}")
            .map { it.lst!! }

    fun getDataByLangWord(langid: Int, word: String): Observable<List<MLangWord>> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataByLangWord("LANGID,eq,$langid", "WORD,eq,${URLEncoder.encode(word, "UTF-8")}")
            // Api is case insensitive
            .map { it.lst!!.filter { it.word == word } }

    fun getDataById(id: Int): Observable<List<MLangWord>> =
        retrofitJson.create(RestLangWord::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun updateNote(id: Int, note: String?): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .updateNote(id, note)
            .map { Log.d("", it.toString()) }

    fun update(id: Int, langid: Int, word: String, note: String?): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .update(id, langid, word, note)
            .map { Log.d("", it.toString()) }

    fun create(langid: Int, word: String, note: String?): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .create(langid, word, note)
            .map { Log.d("", it.toString()) }

    fun delete(o: MLangWord): Observable<Int> =
        retrofitJson.create(RestLangWord::class.java)
            .delete(o.id, o.langid, o.word, o.note, o.famiid, o.level, o.correct, o.total)
            .map { Log.d("", it.toString()) }
}
