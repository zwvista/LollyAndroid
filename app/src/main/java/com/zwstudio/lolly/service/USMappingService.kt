package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MUSMapping
import com.zwstudio.lolly.restapi.RestUSMapping
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class USMappingService: BaseService() {
    fun getData(): Observable<List<MUSMapping>> {
        return retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .map { it.lst!!}
    }
}
