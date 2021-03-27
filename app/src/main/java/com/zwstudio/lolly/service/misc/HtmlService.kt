package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitHtml
import com.zwstudio.lolly.restapi.misc.RestHtml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.EBean

class HtmlService {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    suspend fun getHtml(url: String): String = withContext(Dispatchers.IO) {
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
    }
}
