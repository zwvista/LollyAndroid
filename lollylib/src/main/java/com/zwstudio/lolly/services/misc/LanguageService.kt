package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MLanguage
import com.zwstudio.lolly.restapi.misc.RestLanguage
import io.reactivex.rxjava3.core.Single
import com.zwstudio.lolly.retrofitJson

class LanguageService {
    fun getData(): Single<List<MLanguage>> =
        retrofitJson.create(RestLanguage::class.java)
            .getData()
            .map { it.lst!!}
}
