package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MLanguage
import com.zwstudio.lolly.restapi.misc.RestLanguage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LanguageService {
    suspend fun getData(): List<MLanguage> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestLanguage::class.java)
            .getData()
            .lst!!
    }
}
