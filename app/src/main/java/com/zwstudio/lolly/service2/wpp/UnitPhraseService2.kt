package com.zwstudio.lolly.service2.wpp

import android.util.Log
import com.zwstudio.lolly.domain.misc.MTextbook
import com.zwstudio.lolly.domain.wpp.MUnitPhrase
import com.zwstudio.lolly.rest2api.wpp.Rest2UnitPhrase
import com.zwstudio.lolly.restapi.wpp.RestUnitPhrase
import com.zwstudio.lolly.service.misc.BaseService
import com.zwstudio.lolly.service2.misc.BaseService2
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class UnitPhraseService2: BaseService2() {
    suspend fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): List<MUnitPhrase> =
        retrofitJson2.create(Rest2UnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .lst!!.also {
                for (o in it)
                    o.textbook = textbook
            }

    suspend fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): List<MUnitPhrase> =
        retrofitJson2.create(Rest2UnitPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }

    suspend fun getDataByLangPhrase(langid: Int, phrase: String, lstTextbooks: List<MTextbook>): List<MUnitPhrase> =
        retrofitJson2.create(Rest2UnitPhrase::class.java)
            .getDataByLangPhrase("LANGID,eq,$langid", "PHRASE,eq,$phrase")
            .lst!!.also {
                for (o in it)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
            }

    suspend fun updateSeqNum(id: Int, seqnum: Int) =
        retrofitJson2.create(Rest2UnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .let { Log.d("", it.toString()) }

    suspend fun update(o: MUnitPhrase) =
        retrofitSP2.create(Rest2UnitPhrase::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .let { Log.d("", it.toString()) }

    suspend fun create(o: MUnitPhrase): Int =
        retrofitSP2.create(Rest2UnitPhrase::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .let {
                Log.d("", it.toString())
                it[0][0].newid!!.toInt()
            }

    suspend fun delete(o: MUnitPhrase) =
        retrofitSP2.create(Rest2UnitPhrase::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .let { Log.d("", it.toString()) }
}
