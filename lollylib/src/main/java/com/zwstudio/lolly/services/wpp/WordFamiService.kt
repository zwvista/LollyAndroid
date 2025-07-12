package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.completeDelete
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.debugCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.wpp.MWordFami
import com.zwstudio.lolly.restapi.wpp.RestWordFami
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordFamiService {
    private val api = retrofitJson.create(RestWordFami::class.java)

    private suspend fun getDataByWord(wordid: Int): List<MWordFami> = withContext(Dispatchers.IO) {
        api.getDataByUserWord("USERID,eq,${GlobalUserViewModel.userid}", "WORDID,eq,$wordid").lst
    }

    private suspend fun update(item: MWordFami) = withContext(Dispatchers.IO) {
        api.update(item.id, item).completeUpdate(item.id)
    }

    private suspend fun create(item: MWordFami): Int = withContext(Dispatchers.IO) {
        api.create(item).debugCreate()
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).completeDelete()
    }

    suspend fun update(wordid: Int, isCorrect: Boolean): MWordFami {
        val lst = getDataByWord(wordid)
        val d = if (isCorrect) 1 else 0
        val item = MWordFami().apply {
            userid = GlobalUserViewModel.userid
            this.wordid = wordid
        }
        if (lst.isEmpty()) {
            item.correct = d
            item.total = 1
            item.id = create(item)
        } else {
            val item2 = lst[0]
            item.id = item2.id
            item.correct = item2.correct + d
            item.total = item2.total + 1
            update(item)
        }
        return item
    }
}
