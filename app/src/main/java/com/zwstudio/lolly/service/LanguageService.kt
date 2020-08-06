package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MLanguage
import com.zwstudio.lolly.restapi.RestLanguage
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class LanguageService: BaseService() {
    fun getData(): Observable<List<MLanguage>> =
        retrofitJson.create(RestLanguage::class.java)
            .getData()
            .map { it.lst!!}
}
