package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MLanguage
import com.zwstudio.lolly.restapi.misc.RestLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LanguageService {
    private val api = retrofitJson.create(RestLanguage::class.java)

    suspend fun getData(): List<MLanguage> = withContext(Dispatchers.IO) {
        api.getData().lst
    }
}
