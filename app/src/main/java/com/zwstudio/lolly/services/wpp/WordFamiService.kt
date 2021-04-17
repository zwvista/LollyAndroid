package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.viewmodels.misc.Global
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordFamiService {
    suspend fun getDataByWord(wordid: Int): List<MWordFami> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,${Global.userid}", "WORDID,eq,$wordid")
            .lst!!
    }

    suspend fun update(o: MWordFami) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .update(o.id, o.userid, o.wordid, o.correct, o.total)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun create(o: MWordFami): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .create(o.userid, o.wordid, o.correct, o.total)
            .also { Log.d("API Result", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .let { Log.d("API Result", it.toString()) }
    }
}
