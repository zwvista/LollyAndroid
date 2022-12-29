package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.restapi.wpp.RestPatternWebPage
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import com.zwstudio.lolly.retrofitJson

class PatternWebPageService {
    fun getDataByPattern(patternid: Int): Single<List<MPatternWebPage>> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataByPattern("PATTERNID,eq,$patternid")
            .map { it.lst!! }

    fun getDataById(id: Int): Single<List<MPatternWebPage>> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun updateSeqNum(id: Int, seqnum: Int): Completable =
        retrofitJson.create(RestPatternWebPage::class.java)
            .updateSeqNum(id, seqnum)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun update(o: MPatternWebPage): Completable =
        retrofitJson.create(RestPatternWebPage::class.java)
            .update(o.id, o.patternid, o.seqnum, o.webpageid)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun create(o: MPatternWebPage): Single<Int> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .create(o.patternid, o.seqnum, o.webpageid)
            .doAfterSuccess { println(it.toString()) }

    fun delete(id: Int): Completable =
        retrofitJson.create(RestPatternWebPage::class.java)
            .delete(id)
            .flatMapCompletable { println(it.toString()); Completable.complete() }
}
