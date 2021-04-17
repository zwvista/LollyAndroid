package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.viewmodels.misc.Global
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import io.reactivex.rxjava3.core.Observable

class WordFamiService {
    fun getDataByWord(wordid: Int): Observable<List<MWordFami>> =
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,${Global.userid}", "WORDID,eq,$wordid")
            .map { it.lst!! }

    fun update(o: MWordFami): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .update(o.id, o.userid, o.wordid, o.correct, o.total)
            .map { Log.d("API Result", it.toString()); Unit }

    fun create(o: MWordFami): Observable<Int> =
        retrofitJson.create(RestWordFami::class.java)
            .create(o.userid, o.wordid, o.correct, o.total)
            .doAfterNext { Log.d("API Result", it.toString()) }

    fun delete(id: Int): Observable<Unit> =
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .map { Log.d("API Result", it.toString()); Unit }
}
