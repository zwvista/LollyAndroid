package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MAutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoCorrectService {
    suspend fun getDataByLang(langid: Int): List<MAutoCorrect> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestAutoCorrect::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
    }
}
