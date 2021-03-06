package com.zwstudio.lolly.service.wpp

import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.restapi.wpp.RestPatternWebPage
import com.zwstudio.lolly.service.misc.BaseService
import org.androidannotations.annotations.EBean

@EBean
class PatternWebPageService: BaseService() {
    suspend fun getDataByPattern(patternid: Int): List<MPatternWebPage> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataByPattern("PATTERNID,eq,$patternid")
            .lst!!

    suspend fun getDataById(id: Int): List<MPatternWebPage> =
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataById("ID,eq,$id")
            .lst!!

    suspend fun updateSeqNum(id: Int, seqnum: Int) =
        retrofitJson.create(RestPatternWebPage::class.java)
            .updateSeqNum(id, seqnum)
            .let { println(it.toString()) }

    suspend fun update(o: MPatternWebPage) =
        retrofitJson.create(RestPatternWebPage::class.java)
            .update(o.id, o.patternid, o.seqnum, o.webpageid)
            .let { println(it.toString()) }

    suspend fun create(o: MPatternWebPage): Int =
        retrofitJson.create(RestPatternWebPage::class.java)
            .create(o.patternid, o.seqnum, o.webpageid)
            .also { println(it.toString()) }

    suspend fun delete(id: Int) =
        retrofitJson.create(RestPatternWebPage::class.java)
            .delete(id)
            .let { println(it.toString()) }
}
