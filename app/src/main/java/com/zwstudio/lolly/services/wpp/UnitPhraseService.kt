package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.views.retrofitSP
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.restapi.wpp.RestUnitPhrase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitPhraseService {
    suspend fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .lst!!.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByTextbook(textbook: MTextbook): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbook("TEXTBOOKID,eq,${textbook.id}")
            .lst!!.distinctBy { it.phraseid }.also {
                for (o in it)
                    o.textbook = textbook
            }
    }

    suspend fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun getDataByLangPhrase(langid: Int, phrase: String, lstTextbooks: List<MTextbook>): List<MUnitPhrase> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLangPhrase("LANGID,eq,$langid", "PHRASE,eq,$phrase")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun update(o: MUnitPhrase) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestUnitPhrase::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .let { Log.d("API Result", it.toString()) }
    }

    suspend fun create(o: MUnitPhrase): Int = withContext(Dispatchers.IO) {
        retrofitSP.create(RestUnitPhrase::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .let {
                Log.d("API Result", it.toString())
                it[0][0].newid!!.toInt()
            }
    }

    suspend fun delete(o: MUnitPhrase) = withContext(Dispatchers.IO) {
        retrofitSP.create(RestUnitPhrase::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .let { Log.d("API Result", it.toString()) }
    }
}
