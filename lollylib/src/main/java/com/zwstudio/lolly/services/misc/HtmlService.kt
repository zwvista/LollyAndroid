package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.restapi.misc.RestHtml
import io.reactivex.rxjava3.core.Single

class HtmlService {
    private val apiHtml = retrofitJson.create(RestHtml::class.java)

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    fun getHtml(url: String): Single<String> =
        apiHtml.getStringResponse(url)
            .applyIO()
}
