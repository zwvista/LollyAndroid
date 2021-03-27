package com.zwstudio.lolly.data.words

import com.zwstudio.lolly.data.misc.GlobalConstants
import com.zwstudio.lolly.domain.wpp.MWordFami
import com.zwstudio.lolly.service.wpp.WordFamiService

class WordsFamiViewModel {

    val wordFamiService = WordFamiService()

    private suspend fun getDataByUserWord(userid: Int, wordid: Int): List<MWordFami> =
        wordFamiService.getDataByUserWord(userid, wordid)

    private suspend fun update(o: MWordFami) =
        wordFamiService.update(o.id, o.userid, o.wordid, o.correct, o.total)

    private suspend fun create(o: MWordFami): Int =
        wordFamiService.create(o.userid, o.wordid, o.correct, o.total)

    private suspend fun delete(id: Int) =
        wordFamiService.delete(id)

    suspend fun update(wordid: Int, isCorrect: Boolean): MWordFami {
        val lst = getDataByUserWord(GlobalConstants.userid, wordid)
        val d = if (isCorrect) 1 else 0
        val item = MWordFami().apply {
            userid = GlobalConstants.userid
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
