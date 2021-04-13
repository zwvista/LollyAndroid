package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.ui.retrofitHtml
import com.zwstudio.lolly.restapi.misc.RestHtml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HtmlService {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    suspend fun getHtml(url: String): String = withContext(Dispatchers.IO) {
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
    }
}
