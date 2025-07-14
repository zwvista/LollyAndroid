package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.logDeleteResult
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logUpdateResult
import com.zwstudio.lolly.common.logCreateResult
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.restapi.wpp.RestUnitPhrase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitPhraseService {
    private val api = retrofitJson.create(RestUnitPhrase::class.java)
    private val apiSP = retrofitSP.create(RestUnitPhrase::class.java)

    suspend fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        api.getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .lst.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByTextbook(textbook: MTextbook): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        api.getDataByTextbook("TEXTBOOKID,eq,${textbook.id}")
            .lst.distinctBy { it.phraseid }.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid")
            .lst.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun getDataByLangPhrase(langid: Int, phrase: String, lstTextbooks: List<MTextbook>): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        api.getDataByLangPhrase("LANGID,eq,$langid", "PHRASE,eq,$phrase")
            .lst.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) = withContext(Dispatchers.IO) {
        api.updateSeqNum(id, seqnum).logUpdate(id)
    }

    suspend fun update(item: MUnitPhrase) = withContext(Dispatchers.IO) {
        apiSP.update(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.phraseid, item.phrase, item.translation)
            .logUpdateResult(item.id)
    }

    suspend fun create(item: MUnitPhrase): Int = withContext(Dispatchers.IO) {
        apiSP.create(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.phraseid, item.phrase, item.translation)
            .logCreateResult()
    }

    suspend fun delete(item: MUnitPhrase) = withContext(Dispatchers.IO) {
        apiSP.delete(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.phraseid, item.phrase, item.translation)
            .logDeleteResult()
    }
}
