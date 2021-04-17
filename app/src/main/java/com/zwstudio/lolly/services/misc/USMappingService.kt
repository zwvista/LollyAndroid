package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.models.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import io.reactivex.rxjava3.core.Observable

class USMappingService {
    fun getData(): Observable<List<MUSMapping>> =
        retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .map { it.lst!! }
}
