package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.restapi.misc.RestHtml
import com.zwstudio.lolly.views.applyIO
import com.zwstudio.lolly.views.retrofitHtml
import io.reactivex.rxjava3.core.Single

class HtmlService {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    fun getHtml(url: String): Single<String> =
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
            .applyIO()
}
