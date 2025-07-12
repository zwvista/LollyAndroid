package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.completeDelete
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.debugCreate
import com.zwstudio.lolly.common.logDebug
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogGP
import com.zwstudio.lolly.restapi.blogs.RestLangBlogGP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangBlogGPService {
    private val api = retrofitJson.create(RestLangBlogGP::class.java)

    suspend fun create(item: MLangBlogGP): Int = withContext(Dispatchers.IO) {
        api.create(item).debugCreate()
    }

    suspend fun update(item: MLangBlogGP) = withContext(Dispatchers.IO) {
        api.update(item.id, item).completeUpdate(item.id)
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).completeDelete()
    }
}
