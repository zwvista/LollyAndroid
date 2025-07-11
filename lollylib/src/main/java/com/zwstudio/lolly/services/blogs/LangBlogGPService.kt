package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDebug
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogGP
import com.zwstudio.lolly.restapi.blogs.RestLangBlogGP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangBlogGPService {
    private val api = retrofitJson.create(RestLangBlogGP::class.java)

    suspend fun create(item: MLangBlogGP): Int = withContext(Dispatchers.IO) {
        api.create(item).also { logDebug("Created GP: result=$it") }
    }

    suspend fun update(item: MLangBlogGP) = withContext(Dispatchers.IO) {
        api.update(item.id, item).let { logDebug("Updated GP ID=${item.id}, result=$it") }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).let { logDebug("Deleted GP ID=$id, result=$it") }
    }
}
