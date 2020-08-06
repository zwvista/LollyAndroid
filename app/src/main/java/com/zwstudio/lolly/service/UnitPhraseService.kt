package com.zwstudio.lolly.service

import android.util.Log
import com.zwstudio.lolly.domain.MTextbook
import com.zwstudio.lolly.domain.MUnitPhrase
import com.zwstudio.lolly.restapi.RestUnitPhrase
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class UnitPhraseService: BaseService() {
    fun getDataByTextbookUnitPart(textbook: MTextbook, unitPartFrom: Int, unitPartTo: Int): Observable<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByTextbookUnitPart("TEXTBOOKID,eq,${textbook.id}",
                "UNITPART,bt,${unitPartFrom},${unitPartTo}")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = textbook
                lst
            }

    fun getDataByLang(langid: Int, lstTextbooks: List<MTextbook>): Observable<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLang("LANGID,eq,${langid}")
            .map {
                val lst = it.lst!!
                for (o in lst)
                    o.textbook = lstTextbooks.first { it.id == o.textbookid }
                lst
            }

    fun getDataByLangPhrase(phraseid: Int): Observable<List<MUnitPhrase>> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .getDataByLangPhrase("PHRASEID,eq,$phraseid")
            .map { it.lst }

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Int> =
        retrofitJson.create(RestUnitPhrase::class.java)
            .updateSeqNum(id, seqnum)
            .map { Log.d("", it.toString()) }

    fun update(o: MUnitPhrase): Observable<Int> =
        retrofitSP.create(RestUnitPhrase::class.java)
            .update(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .map { Log.d("", it.toString()) }

    fun create(o: MUnitPhrase): Observable<Int> =
        retrofitSP.create(RestUnitPhrase::class.java)
            .create(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .map {
                Log.d("", it.toString())
                it[0][0].newid!!.toInt()
            }

    fun delete(o: MUnitPhrase): Observable<Int> =
        retrofitSP.create(RestUnitPhrase::class.java)
            .delete(o.id, o.langid, o.textbookid, o.unit, o.part, o.seqnum, o.phraseid, o.phrase, o.translation)
            .map { Log.d("", it.toString()) }
}
