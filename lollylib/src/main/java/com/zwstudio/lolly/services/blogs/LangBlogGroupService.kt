package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDebug
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogGroup
import com.zwstudio.lolly.restapi.blogs.RestLangBlogGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LangBlogGroupService {
    private val api = retrofitJson.create(RestLangBlogGroup::class.java)

    suspend fun getDataByLang(langid: Int): List<MLangBlogGroup> = withContext(Dispatchers.IO) {
        api.getDataByLang("LANGID,eq,$langid", "NAME").lst
    }

    suspend fun getDataByLangPost(langid: Int, postid: Int): List<MLangBlogGroup> = withContext(Dispatchers.IO) {
        api.getDataByLangPost(
            "LANGID,eq,$langid",
            "POSTID,eq,$postid",
            "GROUPNAME"
        ).lst.map { o ->
            MLangBlogGroup(
                id = o.groupid,
                langid = langid,
                groupname = o.groupname,
            ).also { it.gpid = o.id }
        }.distinctBy { it.id }
    }

    suspend fun create(item: MLangBlogGroup): Int = withContext(Dispatchers.IO) {
        api.create(item)
            .also { logDebug("Created new item, result=$it") }
    }

    suspend fun update(item: MLangBlogGroup) = withContext(Dispatchers.IO) {
        api.update(item.id, item)
            .let { logDebug("Updated item ${item.id}, result=$it") }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id)
            .let { logDebug("Deleted item $id, result=$it") }
    }
}
