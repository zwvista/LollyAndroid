package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MUSMapping
import com.zwstudio.lolly.restapi.RestUSMapping
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class USMappingService: BaseService() {
    fun getData(): Observable<List<MUSMapping>> =
        retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .map { it.lst!! }
}
