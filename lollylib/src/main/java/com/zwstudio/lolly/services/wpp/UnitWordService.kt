package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.completeDeleteResult
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.completeUpdateResult
import com.zwstudio.lolly.common.debugCreateResult
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.restapi.wpp.RestUnitWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitWordService {
    private val api = retrofitJson.create(RestUnitWord::class.java)
    private val apiSP = retrofitSP.create(RestUnitWord::class.java)

    suspend fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .lst.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByTextbook(textbook: MTextbook): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByTextbook("TEXTBOOKID,eq,${textbook.id}")
            .lst.distinctBy { it.wordid }.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid")
            .lst.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun getDataByLangWord(langid: Int, word: String, lstTextbooks: List<MTextbook>): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByLangWord("LANGID,eq,$langid", "WORD,eq,$word")
            .lst.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) = withContext(Dispatchers.IO) {
        api.updateSeqNum(id, seqnum).completeUpdate(id)
    }

    suspend fun update(item: MUnitWord) = withContext(Dispatchers.IO) {
        apiSP.update(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.wordid, item.word, item.note, item.famiid, item.correct, item.total)
            .completeUpdateResult(item.id)
    }

    suspend fun create(item: MUnitWord): Int = withContext(Dispatchers.IO) {
        apiSP.create(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.wordid, item.word, item.note, item.famiid, item.correct, item.total)
            .debugCreateResult()
    }

    suspend fun delete(item: MUnitWord) = withContext(Dispatchers.IO) {
        apiSP.delete(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.wordid, item.word, item.note, item.famiid, item.correct, item.total)
            .completeDeleteResult()
    }
}
