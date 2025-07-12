package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.restapi.wpp.RestUnitWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitWordService {
    private val api = retrofitJson.create(RestUnitWord::class.java)

    suspend fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .lst!!.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByTextbook(textbook: MTextbook): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByTextbook("TEXTBOOKID,eq,${textbook.id}")
            .lst!!.distinctBy { it.wordid }.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun getDataByLangWord(langid: Int, word: String, lstTextbooks: List<MTextbook>): List<MUnitWord> = withContext(Dispatchers.IO) {
        api.getDataByLangWord("LANGID,eq,$langid", "WORD,eq,$word")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) = withContext(Dispatchers.IO) {
        api.updateSeqNum(id, seqnum)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun update(o: MUnitWord) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestUnitWord::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun create(o: MUnitWord): Int = withContext(Dispatchers.IO) {
        retrofitSP.create(RestUnitWord::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .let {
                Log.d("API Result", it.toString())
                it[0][0].newid!!.toInt()
            }
    }

    suspend fun delete(o: MUnitWord) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestUnitWord::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .let { Log.d("API Result", it.toString()) }
    }
}
