package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDebug
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogPostContent
import com.zwstudio.lolly.restapi.blogs.RestLangBlogPostContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangBlogPostContentService {
    private val api = retrofitJson.create(RestLangBlogPostContent::class.java)

    suspend fun getDataById(id: Int): MLangBlogPostContent? = withContext(Dispatchers.IO) {
        api.getDataById("ID,eq,$id").lst.firstOrNull()
    }

    suspend fun update(item: MLangBlogPostContent) = withContext(Dispatchers.IO) {
        api.update(item.id, item).let { logDebug("Updated content ID=${item.id}, result=$it") }
    }
}
