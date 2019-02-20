package com.zwstudio.lolly.data

import android.util.Log
import com.zwstudio.lolly.domain.WordFami
import com.zwstudio.lolly.restapi.RestWordFami
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordsFamiViewModel : BaseViewModel1() {

    val userid = 1

    private fun getDataByUserWord(userid: Int, wordid: Int): Observable<List<WordFami>> =
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,$userid", "WORDID,eq,$wordid")
            .map { it.lst!! }
            .applyIO()

    private fun update(id: Int, userid: Int, wordid: Int, level: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .update(id, userid, wordid, level)
            .map { Log.d("", it.toString()) }
            .applyIO()

    private fun create(userid: Int, wordid: Int, level: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .create(userid, wordid, level)
            .map { Log.d("", it.toString()) }
            .applyIO()

    private fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
            .applyIO()

    fun update(wordid: Int, level: Int): Observable<Int> {
        return getDataByUserWord(userid, wordid).concatMap { lst ->
            if (lst.isEmpty()) {
                if (level == 0)
                    Observable.empty<Int>()
                else
                    create(userid, wordid, level)
            } else {
                val id = lst[0].id
                if (level == 0)
                    delete(id)
                else
                    update(id, userid, wordid, level)
            }
        }
    }

}
