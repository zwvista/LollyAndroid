package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.GlobalUser
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import com.zwstudio.lolly.retrofitJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordFamiService {
    private suspend fun getDataByWord(wordid: Int): List<MWordFami> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .getDataByUserWord("USERID,eq,${GlobalUser.userid}", "WORDID,eq,$wordid")
            .lst!!
    }

    private suspend fun update(o: MWordFami) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .update(o.id, o.userid, o.wordid, o.correct, o.total)
            .let { Log.d("API Result", it.toString()) }
    }

    private suspend fun create(o: MWordFami): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .create(o.userid, o.wordid, o.correct, o.total)
            .also { Log.d("API Result", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWordFami::class.java)
            .delete(id)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun update(wordid: Int, isCorrect: Boolean): MWordFami {
        val lst = getDataByWord(wordid)
        val d = if (isCorrect) 1 else 0
        val item = MWordFami().apply {
            userid = GlobalUser.userid
            this.wordid = wordid
        }
        if (lst.isEmpty()) {
            item.correct = d
            item.total = 1
            item.id = create(item)
        }
        else {
            val o = lst[0]
            item.id = o.id
            item.correct = o.correct + d
            item.total = o.total + 1
            update(item)
        }
        return item
    }
}
