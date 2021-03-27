package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MUSMapping
import com.zwstudio.lolly.restapi.misc.RestUSMapping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class USMappingService {
    suspend fun getData(): List<MUSMapping> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUSMapping::class.java)
            .getData()
            .lst!!
    }
}
