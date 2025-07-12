package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class USMappingService {
    private val api = retrofitJson.create(RestUSMapping::class.java)

    suspend fun getData(): List<MUSMapping> = withContext(Dispatchers.IO) {
        api.getData().lst
    }
}
