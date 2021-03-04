package com.zwstudio.lolly.service2.wpp

import android.util.Log
import com.zwstudio.lolly.domain.misc.MTextbook
import com.zwstudio.lolly.domain.wpp.MUnitWord
import com.zwstudio.lolly.rest2api.wpp.Rest2UnitWord
import com.zwstudio.lolly.restapi.wpp.RestUnitWord
import com.zwstudio.lolly.service.misc.BaseService
import com.zwstudio.lolly.service2.misc.BaseService2
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class UnitWordService2: BaseService2() {
    suspend fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): List<MUnitWord> =
        retrofitJson2.create(Rest2UnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .lst!!.also {
                for (o in it)
                    o.textbook = textbook
            }

    suspend fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): List<MUnitWord> =
        retrofitJson2.create(Rest2UnitWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }

    suspend fun getDataByLangWord(langid: Int, word: String, lstTextbooks: List<MTextbook>): List<MUnitWord> =
        retrofitJson2.create(Rest2UnitWord::class.java)
            .getDataByLangWord("LANGID,eq,$langid", "WORD,eq,$word")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }

    suspend fun updateSeqNum(id: Int, seqnum: Int) =
        retrofitJson2.create(Rest2UnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .let { Log.d("", it.toString()) }

    suspend fun updateNote(id: Int, note: String?) =
        retrofitJson2.create(Rest2UnitWord::class.java)
            .updateNote(id, note)
            .let { Log.d("", it.toString()) }

    suspend fun update(o: MUnitWord) =
        retrofitSP2.create(Rest2UnitWord::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .let { Log.d("", it.toString()) }

    suspend fun create(o: MUnitWord): Int =
        retrofitSP2.create(Rest2UnitWord::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .let {
                Log.d("", it.toString())
                it[0][0].newid!!.toInt()
            }

    suspend fun delete(o: MUnitWord) =
        retrofitSP2.create(Rest2UnitWord::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .let { Log.d("", it.toString()) }
}
