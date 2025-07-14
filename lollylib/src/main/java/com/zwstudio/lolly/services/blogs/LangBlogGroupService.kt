package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogGroup
import com.zwstudio.lolly.restapi.blogs.RestLangBlogGroup
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangBlogGroupService {
    private val api = retrofitJson.create(RestLangBlogGroup::class.java)

    fun getDataByLang(langid: Int): Single<List<MLangBlogGroup>> =
        api.getDataByLang("LANGID,eq,$langid", "NAME").map {
            it.lst
        }

    fun getDataByLangPost(langid: Int, postid: Int): Single<List<MLangBlogGroup>> =
        api.getDataByLangPost("LANGID,eq,$langid", "POSTID,eq,$postid").map {
            it.lst.map { item ->
                MLangBlogGroup(
                    id = item.groupid,
                    langid = langid,
                    groupname = item.groupname,
                ).also { it.gpid = item.id }
            }.distinctBy { it.id }
        }

    fun create(item: MLangBlogGroup): Single<Int> =
        api.create(item).logCreate()

    fun update(item: MLangBlogGroup): Completable =
        api.update(item.id, item).logUpdate(item.id)

    fun delete(id: Int): Completable =
        api.delete(id).logDelete()
}
