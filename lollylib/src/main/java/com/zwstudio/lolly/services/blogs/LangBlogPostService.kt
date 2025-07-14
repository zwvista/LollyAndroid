package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.restapi.blogs.RestLangBlogPost
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangBlogPostService {
    private val api = retrofitJson.create(RestLangBlogPost::class.java)

    fun getDataByLang(langid: Int): Single<List<MLangBlogPost>> =
        api.getDataByLang("LANGID,eq,$langid").map {
            it.lst
        }

    fun getDataByLangGroup(langid: Int, groupid: Int): Single<List<MLangBlogPost>> =
        api.getDataByLangGroup("LANGID,eq,$langid", "GROUPID,eq,$groupid").map {
            it.lst.map { item ->
                MLangBlogPost(
                    id = item.postid,
                    langid = langid,
                    title = item.title,
                    url = item.url
                ).also { it.gpid = item.id }
            }.distinctBy { it.id }
        }

    fun create(item: MLangBlogPost): Single<Int> =
        api.create(item).logCreate()

    fun update(item: MLangBlogPost): Completable =
        api.update(item.id, item).logUpdate(item.id)

    fun delete(id: Int): Completable =
        api.delete(id).logDelete()
}
