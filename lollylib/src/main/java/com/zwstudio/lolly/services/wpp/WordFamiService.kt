package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import com.zwstudio.lolly.services.misc.Global
import com.zwstudio.lolly.services.misc.retrofitJson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class WordFamiService {
    private fun getDataByWord(wordid: Int): Single<List<MWordFami>> =
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,${Global.userid}", "WORDID,eq,$wordid")
            .map { it.lst!! }

    private fun update(o: MWordFami): Completable =
        retrofitJson.create(RestWordFami::class.java)
            .update(o.id, o.userid, o.wordid, o.correct, o.total)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    private fun create(o: MWordFami): Single<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .create(o.userid, o.wordid, o.correct, o.total)
            .doAfterSuccess { Log.d("API Result", it.toString()) }

    fun delete(id: Int): Completable =
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(wordid: Int, isCorrect: Boolean): Single<MWordFami> =
        getDataByWord(wordid).flatMap { lst ->
            val d = if (isCorrect) 1 else 0
            val item = MWordFami().apply {
                userid = Global.userid
                this.wordid = wordid
            }
            if (lst.isEmpty()) {
                item.correct = d
                item.total = 1
                create(item).map {
                    item.id = it
                    item
                }
            }
            else {
                val o = lst[0]
                item.id = o.id
                item.correct = o.correct + d
                item.total = o.total + 1
                update(item).toSingle {
                    item
                }
            }
        }
}
