package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import io.reactivex.rxjava3.core.Observable

class USMappingService {
    fun getData(): Observable<List<MUSMapping>> =
        retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .map { it.lst!! }
}
