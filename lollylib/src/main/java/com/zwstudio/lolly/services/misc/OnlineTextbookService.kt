package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MOnlineTextbook
import com.zwstudio.lolly.restapi.misc.RestOnlineTextbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnlineTextbookService {
    private val api = retrofitJson.create(RestOnlineTextbook::class.java)

    suspend fun getDataByLang(langid: Int): List<MOnlineTextbook> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid").lst
    }
}
