package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import io.reactivex.rxjava3.core.Observable

class WordFamiService {
    fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> =
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,$userid", "WORDID,eq,$wordid")
            .map { it.lst!! }

    fun update(id: Int, userid: Int, wordid: Int, correct: Int, total: Int): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .update(id, userid, wordid, correct, total)
            .map { Log.d("API Result", it.toString()); Unit }

    fun create(userid: Int, wordid: Int, correct: Int, total: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .create(userid, wordid, correct, total)
            .doAfterNext { Log.d("API Result", it.toString()) }

    fun delete(id: Int): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .map { Log.d("API Result", it.toString()); Unit }
}
