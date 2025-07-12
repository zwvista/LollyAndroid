package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import io.reactivex.rxjava3.core.Single

class USMappingService {
    private val api = retrofitJson.create(RestUSMapping::class.java)

    fun getData(): Single<List<MUSMapping>> =
        api.getData()
            .map { it.lst }
}
