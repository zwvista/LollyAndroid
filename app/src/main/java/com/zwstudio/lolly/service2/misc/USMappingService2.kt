package com.zwstudio.lolly.service2.misc

import com.zwstudio.lolly.domain.misc.MUSMapping
import com.zwstudio.lolly.rest2api.misc.Rest2USMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class USMappingService2: BaseService2() {
    suspend fun getData(): List<MUSMapping> =
        retrofitJson2.create(Rest2USMapping::class.java)
            .getData()
            .lst!!
}
