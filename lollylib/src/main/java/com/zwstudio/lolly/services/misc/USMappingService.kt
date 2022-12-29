package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import io.reactivex.rxjava3.core.Single
import com.zwstudio.lolly.retrofitJson

class USMappingService {
    fun getData(): Single<List<MUSMapping>> =
        retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .map { it.lst!! }
}
