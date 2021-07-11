package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import com.zwstudio.lolly.views.retrofitJson
import io.reactivex.rxjava3.core.Single

class USMappingService {
    fun getData(): Single<List<MUSMapping>> =
        retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .map { it.lst!! }
}
