package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
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
        api.getDataByLangPost("LANGID,eq,$langid", "POSTID,eq,$postid")
            .lst.map { item ->
                MLangBlogGroup(
                    id = item.groupid,
                    langid = langid,
                    groupname = item.groupname,
                ).also { it.gpid = item.id }
            }.distinctBy { it.id }
    }

    suspend fun create(item: MLangBlogGroup): Int = withContext(Dispatchers.IO) {
        api.create(item).logCreate()
    }

    suspend fun update(item: MLangBlogGroup) = withContext(Dispatchers.IO) {
        api.update(item.id, item).logUpdate(item.id)
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        api.delete(id).logDelete()
    }
}
