package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.GlobalConstants
import com.zwstudio.lolly.data.misc.GlobalConstants.userid
import com.zwstudio.lolly.domain.wpp.MWordFami
import com.zwstudio.lolly.service.wpp.WordFamiService
import org.androidannotations.annotations.EBean

@EBean
class WordsFamiViewModel {

    val wordFamiService = WordFamiService()

    private suspend fun getDataByUserWord(userid: Int, wordid: Int): List<MWordFami> =
        wordFamiService.getDataByUserWord(userid, wordid)

    private suspend fun update(id: Int, userid: Int, wordid: Int, correct: Int, total: Int) =
        wordFamiService.update(id, userid, wordid, correct, total)

    private suspend fun create(userid: Int, wordid: Int, correct: Int, total: Int) =
        wordFamiService.create(userid, wordid, correct, total)

    private suspend fun delete(id: Int) =
        wordFamiService.delete(id)

    suspend fun update(wordid: Int, isCorrect: Boolean) {
        val lst = getDataByUserWord(GlobalConstants.userid, wordid)
        val d = if (isCorrect) 1 else 0
        if (lst.isEmpty())
            create(userid, wordid, d, 1)
        else {
            val o = lst[0]
            update(o.id, userid, o.wordid,  o.correct + d, o.total + 1)
        }
    }
}
