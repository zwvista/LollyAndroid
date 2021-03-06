package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.domain.misc.MLanguage
import com.zwstudio.lolly.restapi.misc.RestLanguage
import org.androidannotations.annotations.EBean

@EBean
class LanguageService: BaseService() {
    suspend fun getData(): List<MLanguage> =
        retrofitJson.create(RestLanguage::class.java)
            .getData()
            .lst!!
}
