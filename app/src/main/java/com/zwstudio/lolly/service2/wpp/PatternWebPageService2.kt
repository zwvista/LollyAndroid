package com.zwstudio.lolly.service2.wpp

import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.rest2api.wpp.Rest2PatternWebPage
import com.zwstudio.lolly.restapi.wpp.RestPatternWebPage
import com.zwstudio.lolly.service.misc.BaseService
import com.zwstudio.lolly.service2.misc.BaseService2
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class PatternWebPageService2: BaseService2() {
    suspend fun getDataByPattern(patternid: Int): List<MPatternWebPage> =
        retrofitJson2.create(Rest2PatternWebPage::class.java)
            .getDataByPattern("PATTERNID,eq,$patternid")
            .lst!!

    suspend fun getDataById(id: Int): List<MPatternWebPage> =
        retrofitJson2.create(Rest2PatternWebPage::class.java)
            .getDataById("ID,eq,$id")
            .lst!!

    suspend fun updateSeqNum(id: Int, seqnum: Int) =
        retrofitJson2.create(Rest2PatternWebPage::class.java)
            .updateSeqNum(id, seqnum)
            .let { println(it.toString()) }

    suspend fun update(o: MPatternWebPage) =
        retrofitJson2.create(Rest2PatternWebPage::class.java)
            .update(o.id, o.patternid, o.seqnum, o.webpageid)
            .let { println(it.toString()) }

    suspend fun create(o: MPatternWebPage): Int =
        retrofitJson2.create(Rest2PatternWebPage::class.java)
            .create(o.patternid, o.seqnum, o.webpageid)
            .also { println(it.toString()) }

    suspend fun delete(id: Int) =
        retrofitJson2.create(Rest2PatternWebPage::class.java)
            .delete(id)
            .let { println(it.toString()) }
}
