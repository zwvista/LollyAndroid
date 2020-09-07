package com.zwstudio.lolly.service

import android.util.Log
import com.zwstudio.lolly.domain.MWordFami
import com.zwstudio.lolly.restapi.RestWordFami
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordFamiService: BaseService() {
    fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> =
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,$userid", "WORDID,eq,$wordid")
            .map { it.lst!! }

    fun update(id: Int, userid: Int, wordid: Int, correct: Int, total: Int): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .update(id, userid, wordid, correct, total)
            .map { Log.d("", it.toString()); Unit }

    fun create(userid: Int, wordid: Int, correct: Int, total: Int): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .create(userid, wordid, correct, total)
            .map { Log.d("", it.toString()); Unit }

    fun delete(id: Int): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .map { Log.d("", it.toString()); Unit }
}
