package com.zwstudio.lolly.service2.misc

import com.zwstudio.lolly.domain.misc.MLanguage
import com.zwstudio.lolly.rest2api.misc.Rest2Language
import com.zwstudio.lolly.restapi.misc.RestLanguage
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class LanguageService2: BaseService2() {
    suspend fun getData(): List<MLanguage> =
        retrofitJson2.create(Rest2Language::class.java)
            .getData()
            .lst!!
}
