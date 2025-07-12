package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.completeDelete
import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.debugCreate
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

    fun create(item: MLangBlogPost): Single<Int> =
        api.create(item).debugCreate()

    fun update(item: MLangBlogPost): Completable =
        api.update(item.id, item).completeUpdate(item.id)

    fun delete(id: Int): Completable =
        api.delete(id).completeDelete()
}
