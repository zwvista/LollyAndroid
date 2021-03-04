package com.zwstudio.lolly.service2.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MWordFami
import com.zwstudio.lolly.rest2api.wpp.Rest2WordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import com.zwstudio.lolly.service.misc.BaseService
import com.zwstudio.lolly.service2.misc.BaseService2
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class WordFamiService2: BaseService2() {
    suspend fun getDataByUserWord(userid: Int, wordid: Int): List<MWordFami> =
        retrofitJson2.create(Rest2WordFami::class.java)
            .getDataByUserWord("USERID,eq,$userid", "WORDID,eq,$wordid")
            .lst!!

    suspend fun update(id: Int, userid: Int, wordid: Int, correct: Int, total: Int) =
        retrofitJson2.create(Rest2WordFami::class.java)
            .update(id, userid, wordid, correct, total)
            .let { Log.d("", it.toString()) }

    suspend fun create(userid: Int, wordid: Int, correct: Int, total: Int) =
        retrofitJson2.create(Rest2WordFami::class.java)
            .create(userid, wordid, correct, total)
            .let { Log.d("", it.toString()) }

    suspend fun delete(id: Int) =
        retrofitJson2.create(Rest2WordFami::class.java)
            .delete(id)
            .let { Log.d("", it.toString()) }
}
