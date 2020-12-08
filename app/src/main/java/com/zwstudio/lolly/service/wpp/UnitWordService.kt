package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.misc.MTextbook
import com.zwstudio.lolly.domain.wpp.MUnitWord
import com.zwstudio.lolly.restapi.wpp.RestUnitWord
import com.zwstudio.lolly.service.misc.BaseService
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class UnitWordService: BaseService() {
    fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): Observable<List<MUnitWord>> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,$unitPartFrom,$unitPartTo")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): Observable<List<MUnitWord>> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun getDataByLangWord(langid: Int, word: String, lstTextbooks: List<MTextbook>): Observable<List<MUnitWord>> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByLangWord("LANGID,eq,$langid", "WORD,eq,$word")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Unit> =
        retrofitJson.create(RestUnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()); Unit }

    fun updateNote(id: Int, note: String?): Observable<Unit> =
        retrofitJson.create(RestUnitWord::class.java)
            .updateNote(id, note)
            .map { Log.d("", it.toString()); Unit }

    fun update(o: MUnitWord): Observable<Unit> =
        retrofitSP.create(RestUnitWord::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .map { Log.d("", it.toString()); Unit }

    fun create(o: MUnitWord): Observable<Int> =
        retrofitSP.create(RestUnitWord::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .map {
                Log.d("", it.toString())
                it[0][0].newid!!.toInt()
            }

    fun delete(o: MUnitWord): Observable<Unit> =
        retrofitSP.create(RestUnitWord::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.wordid, o.word, o.note, o.famiid, o.correct, o.total)
            .map { Log.d("", it.toString()); Unit }
}