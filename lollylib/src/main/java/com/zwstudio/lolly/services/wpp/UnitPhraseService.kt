package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.lolly.models.wpp.MUnitPhrase
import com.zwstudio.lolly.restapi.wpp.RestUnitPhrase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UnitPhraseService {
    fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): Single<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByTextbook(textbook: MTextbook): Single<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbook("TEXTBOOKID,eq,${textbook.id}")
            .map {
                val lst = it.lst!!.distinctBy { it.phraseid }
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): Single<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun getDataByLangPhrase(langid: Int, phrase: String, lstTextbooks: List<MTextbook>): Single<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLangPhrase("LANGID,eq,$langid", "PHRASE,eq,$phrase")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun updateSeqNum(id: Int, seqnum: Int): Completable =
        retrofitJson.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun update(o: MUnitPhrase): Completable =
        retrofitSP.create(RestUnitPhrase::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }

    fun create(o: MUnitPhrase): Single<Int> =
        retrofitSP.create(RestUnitPhrase::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .map {
                Log.d("API Result", it.toString())
                it[0][0].newid!!.toInt()
            }

    fun delete(o: MUnitPhrase): Completable =
        retrofitSP.create(RestUnitPhrase::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .flatMapCompletable { Log.d("API Result", it.toString()); Completable.complete() }
}
