package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.restapi.misc.RestHtml
import org.androidannotations.annotations.EBean

@EBean
class HtmlService: BaseService() {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    suspend fun getHtml(url: String): String =
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
}
