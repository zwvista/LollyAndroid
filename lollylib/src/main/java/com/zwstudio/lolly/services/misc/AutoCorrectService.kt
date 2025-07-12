package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MAutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoCorrectService {
    private val api = retrofitJson.create(RestAutoCorrect::class.java)

    suspend fun getDataByLang(langid: Int): List<MAutoCorrect> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid")
            .lst!!
    }
}
