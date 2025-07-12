package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitHtml
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.restapi.misc.RestHtml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HtmlService {
    private val api = retrofitHtml.create(RestHtml::class.java)

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    suspend fun getHtml(url: String): String = withContext(Dispatchers.IO) {
        api.getStringResponse(url)
    }
}
