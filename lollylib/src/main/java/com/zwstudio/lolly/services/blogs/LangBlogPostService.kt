package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDebug
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.models.blogs.MLangBlogPosts
import com.zwstudio.lolly.restapi.blogs.RestLangBlogPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangBlogPostService {
    private val api = retrofitJson.create(RestLangBlogPost::class.java)

    suspend fun getDataByLang(langid: Int): List<MLangBlogPost> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid").lst ?: emptyList()
    }

    suspend fun update(item: MLangBlogPost) = withContext(Dispatchers.IO) {
        api.update(item.id, item).let { logDebug("Updated ID=${item.id}, result=$it") }
    }

    suspend fun create(item: MLangBlogPost) = withContext(Dispatchers.IO) {
        api.create(item).also { logDebug("Created new item, result=$it") }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).let { logDebug("Deleted ID=$id, result=$it") }
    }
}
