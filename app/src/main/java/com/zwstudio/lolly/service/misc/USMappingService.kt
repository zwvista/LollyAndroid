package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.domain.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import org.androidannotations.annotations.EBean

@EBean
class USMappingService: BaseService() {
    suspend fun getData(): List<MUSMapping> =
        retrofitJson2.create(RestUSMapping::class.java)
            .getData()
            .lst!!
}
