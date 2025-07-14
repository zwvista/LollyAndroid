package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logDelete
import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MLangBlogGP
import com.zwstudio.lolly.restapi.blogs.RestLangBlogGP
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class LangBlogGPService {
    private val api = retrofitJson.create(RestLangBlogGP::class.java)

    fun create(item: MLangBlogGP): Single<Int> =
        api.create(item).logCreate()

    fun update(item: MLangBlogGP): Completable =
        api.update(item.id, item).logUpdate(item.id)

    fun delete(id: Int): Completable =
        api.delete(id).logDelete()
}
