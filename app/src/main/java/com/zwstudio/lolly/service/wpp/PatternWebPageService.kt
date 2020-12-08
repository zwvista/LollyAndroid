package com.zwstudio.lolly.service.wpp

import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.restapi.wpp.RestPatternWebPage
import com.zwstudio.lolly.service.misc.BaseService
import io.reactivex.rxjava3.core.Observable

class PatternWebPageService: BaseService() {
    fun getDataByPattern(patternid: Int): Observable<List<MPatternWebPage>> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataByPattern("PATTERNID,eq,$patternid")
            .map { it.lst!! }

    fun getDataById(id: Int): Observable<List<MPatternWebPage>> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun updateSeqNum(id: Int, seqnum: Int): Observable<Unit> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .updateSeqNum(id, seqnum)
            .map { println(it.toString()) }

    fun update(o: MPatternWebPage): Observable<Unit> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .update(o.id, o.patternid, o.seqnum, o.webpageid)
            .map { println(it.toString()) }

    fun create(o: MPatternWebPage): Observable<Int> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .create(o.patternid, o.seqnum, o.webpageid)
            .doOnNext { println(it.toString()) }

    fun delete(id: Int): Observable<Unit> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .delete(id)
            .map { println(it.toString()) }
}
