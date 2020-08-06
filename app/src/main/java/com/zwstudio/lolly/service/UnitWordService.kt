package com.zwstudio.lolly.service

import android.util.Log
import com.zwstudio.lolly.domain.MTextbook
import com.zwstudio.lolly.domain.MUnitWord
import com.zwstudio.lolly.restapi.RestUnitWord
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class UnitWordService: BaseService() {
    fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): Observable<List<MUnitWord>> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,${unitPartFrom},${unitPartTo}")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): Observable<List<MUnitWord>> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByLang("LANGID,eq,${langid}")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun getDataByLangWord(wordid: Int): Observable<List<MUnitWord>> =
        retrofitJson.create(RestUnitWord::class.java)
            .getDataByLangWord("WORDID,eq,$wordid")
            .map { it.lst!! }

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()) }

    fun updateNote(id: Int, note: String?): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .updateNote(id, note)
            .map { Log.d("", it.toString()) }

    fun update(id: Int, textbookid: Int, unit: Int, part: Int, seqnum: Int,  wordid: Int): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .update(id, textbookid, unit, part, seqnum, wordid)
            .map { Log.d("", it.toString()) }

    fun create(textbookid: Int, unit: Int, part: Int, seqnum: Int,  wordid: Int): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .create(textbookid, unit, part, seqnum, wordid)
            .map { Log.d("", it.toString()) }

    fun delete(id: Int): Observable<Int> =
        retrofitJson.create(RestUnitWord::class.java)
            .delete(id)
            .map { Log.d("", it.toString()) }
}
