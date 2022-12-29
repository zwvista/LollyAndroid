package com.zwstudio.lolly.services.wpp

import android.util.Log
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.restapi.wpp.RestPatternWebPage
import com.zwstudio.lolly.services.misc.retrofitJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatternWebPageService {
    suspend fun getDataByPattern(patternid: Int): List<MPatternWebPage> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataByPattern("PATTERNID,eq,$patternid")
            .lst!!
    }

    suspend fun getDataById(id: Int): List<MPatternWebPage> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPatternWebPage::class.java)
            .getDataById("ID,eq,$id")
            .lst!!
    }

    suspend fun updateSeqNum(id: Int, seqnum: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPatternWebPage::class.java)
            .updateSeqNum(id, seqnum)
            .let { Log.d("", it.toString()) }
    }

    suspend fun update(o: MPatternWebPage) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPatternWebPage::class.java)
            .update(o.id, o.patternid, o.seqnum, o.webpageid)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MPatternWebPage): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPatternWebPage::class.java)
            .create(o.patternid, o.seqnum, o.webpageid)
            .also { Log.d("", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestPatternWebPage::class.java)
            .delete(id)
            .let { Log.d("", it.toString()) }
    }
}
