package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MWebTextbook
import com.zwstudio.lolly.restapi.misc.RestWebTextbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WebTextbookService {
    suspend fun getDataByLang(langid: Int): List<MWebTextbook> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebTextbook::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
    }
}
