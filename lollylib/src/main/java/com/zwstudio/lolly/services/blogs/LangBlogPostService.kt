package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.completeDelete
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.debugCreate
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
        api.getDataByLang("LANGID,eq,$langid").lst
    }

    suspend fun create(item: MLangBlogPost) = withContext(Dispatchers.IO) {
        api.create(item).debugCreate()
    }

    suspend fun update(item: MLangBlogPost) = withContext(Dispatchers.IO) {
        api.update(item.id, item).completeUpdate(item.id)
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).completeDelete()
    }
}
