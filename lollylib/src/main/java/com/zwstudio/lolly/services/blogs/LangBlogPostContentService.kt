package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogPostContent
import com.zwstudio.lolly.restapi.blogs.RestLangBlogPostContent
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.Optional

class LangBlogPostContentService {
    private val api = retrofitJson.create(RestLangBlogPostContent::class.java)

    fun getDataById(id: Int): Single<Optional<MLangBlogPostContent>> =
        api.getDataById("ID,eq,$id").map {
            Optional.ofNullable(it.lst.firstOrNull())
        }

    fun update(item: MLangBlogPostContent): Completable =
        api.update(item.id, item).logUpdate(item.id)
}
