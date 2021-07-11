package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MLanguage
import com.zwstudio.lolly.restapi.misc.RestLanguage
import com.zwstudio.lolly.views.retrofitJson
import io.reactivex.rxjava3.core.Single

class LanguageService {
    fun getData(): Single<List<MLanguage>> =
        retrofitJson.create(RestLanguage::class.java)
            .getData()
            .map { it.lst!!}
}
