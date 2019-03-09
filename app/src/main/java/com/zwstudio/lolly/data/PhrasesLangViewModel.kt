package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.MLangPhrase
import com.zwstudio.lolly.restapi.RestLangPhrase
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class PhrasesLangViewModel : BaseViewModel2() {

    var lstPhrases = mutableListOf<MLangPhrase>()
    var isSwipeStarted = false

    fun getData(): Observable<Unit> =
        retrofitJson.create(RestLangPhrase::class.java)
            .getDataByLang("LANGID,eq,${vmSettings.selectedLang.id}")
            .map { lstPhrases = it.lst!!.toMutableList() }
            .applyIO()

    fun update(id: Int, langid: Int, phrase: String, translation: String?): Observable<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .update(id, langid, phrase, translation)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun create(langid: Int, phrase: String, translation: String?): Observable<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .create(langid, phrase, translation)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestLangPhrase::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun newLangPhrase() = MLangPhrase().apply {
        langid = vmSettings.selectedLang.id
    }
}
