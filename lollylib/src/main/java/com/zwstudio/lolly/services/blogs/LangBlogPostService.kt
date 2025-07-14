package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.restapi.blogs.RestLangBlogPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangBlogPostService {
    private val api = retrofitJson.create(RestLangBlogPost::class.java)

    suspend fun getDataByLang(langid: Int): List<MLangBlogPost> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid").lst
    }

    suspend fun getDataByLangGroup(langid: Int, groupid: Int): List<MLangBlogPost> = withContext(Dispatchers.IO) {
        api.getDataByLangGroup("LANGID,eq,$langid", "GROUPID,eq,$groupid")
            .lst.map { item ->
                MLangBlogPost(
                    id = item.postid,
                    langid = langid,
                    title = item.title,
                    url = item.url
                ).also { it.gpid = item.id }
            }.distinctBy { it.id }
    }

    suspend fun create(item: MLangBlogPost) = withContext(Dispatchers.IO) {
        api.create(item).logCreate()
    }

    suspend fun update(item: MLangBlogPost) = withContext(Dispatchers.IO) {
        api.update(item.id, item).logUpdate(item.id)
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).logDelete()
    }
}
