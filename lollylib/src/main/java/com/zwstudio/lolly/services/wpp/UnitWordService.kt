package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.common.logDeleteResult
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logUpdateResult
import com.zwstudio.lolly.common.logCreateResult
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.common.retrofitSP
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.lolly.models.wpp.MUnitWord
import com.zwstudio.lolly.restapi.wpp.RestUnitWord
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UnitWordService {
    private val api = retrofitJson.create(RestUnitWord::class.java)
    private val apiSP = retrofitSP.create(RestUnitWord::class.java)

    fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): Single<List<MUnitWord>> =
        api.getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .map {
                val lst = it.lst
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByTextbook(textbook: MTextbook): Single<List<MUnitWord>> =
        api.getDataByTextbook("TEXTBOOKID,eq,${textbook.id}")
            .map {
                val lst = it.lst.distinctBy { it.wordid }
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): Single<List<MUnitWord>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map {
                val lst = it.lst

                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun getDataByLangWord(langid: Int, word: String, lstTextbooks: List<MTextbook>): Single<List<MUnitWord>> =
        api.getDataByLangWord("LANGID,eq,$langid", "WORD,eq,$word")
            .map {
                val lst = it.lst

                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun updateSeqNum(id: Int, seqnum: Int): Completable =
        api.updateSeqNum(id, seqnum).logUpdate(id)

    fun update(item: MUnitWord): Completable =
        apiSP.update(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.wordid, item.word, item.note, item.famiid, item.correct, item.total)
            .logUpdateResult(item.id)

    fun create(item: MUnitWord): Single<Int> =
        apiSP.create(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.wordid, item.word, item.note, item.famiid, item.correct, item.total)
            .logCreateResult()

    fun delete(item: MUnitWord): Completable =
        apiSP.delete(item.id, item.langid, item.textbookid, item.unit, item.part, item.seqnum, item.wordid, item.word, item.note, item.famiid, item.correct, item.total)
            .logDeleteResult()
}
