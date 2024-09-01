package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MOnlineTextbook
import com.zwstudio.lolly.restapi.misc.RestOnlineTextbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnlineTextbookService {
    suspend fun getDataByLang(langid: Int): List<MOnlineTextbook> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestOnlineTextbook::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
    }
}
