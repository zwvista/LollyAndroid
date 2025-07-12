package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class WordFamiService {
    private val api = retrofitJson.create(RestWordFami::class.java)

    private fun getDataByWord(wordid: Int): Single<List<MWordFami>> =
        api.getDataByUserWord("USERID,eq,${GlobalUserViewModel.userid}", "WORDID,eq,$wordid")
            .map { it.lst }

    private fun update(o: MWordFami): Completable =
        api.update(o.id, o.userid, o.wordid, o.correct, o.total)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    private fun create(o: MWordFami): Single<Int> =
        api.create(o.userid, o.wordid, o.correct, o.total)
            .doAfterSuccess { Log.d("API Result", it.toString()) }

    fun delete(id: Int): Completable =
        api.delete(id)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(wordid: Int, isCorrect: Boolean): Single<MWordFami> =
        getDataByWord(wordid).flatMap { lst ->
            val d = if (isCorrect) 1 else 0
            val item = MWordFami().apply {
                userid = GlobalUserViewModel.userid
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
