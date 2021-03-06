package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import com.zwstudio.lolly.service.misc.BaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.EBean

@EBean
class WordFamiService: BaseService() {
    suspend fun getDataByUserWord(userid: Int, wordid: Int): List<MWordFami> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,$userid", "WORDID,eq,$wordid")
            .lst!!
    }

    suspend fun update(id: Int, userid: Int, wordid: Int, correct: Int, total: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .update(id, userid, wordid, correct, total)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(userid: Int, wordid: Int, correct: Int, total: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .create(userid, wordid, correct, total)
            .let { Log.d("", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .let { Log.d("", it.toString()) }
    }
}
