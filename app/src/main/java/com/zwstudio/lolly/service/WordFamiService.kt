package com.zwstudio.lolly.service

import android.util.Log
import com.zwstudio.lolly.domain.MWordFami
import com.zwstudio.lolly.restapi.RestWordFami
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordFamiService: BaseService() {
    fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<MWordFami>> {
        return retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,$userid", "WORDID,eq,$wordid")
            .map { it.lst!! }
    }

    fun update(id: Int, userid: Int, wordid: Int, level: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .update(id, userid, wordid, level)
            .map { Log.d("", it.toString()) }

    fun create(userid: Int, wordid: Int, level: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .create(userid, wordid, level)
            .map { Log.d("", it.toString()) }

    fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
}