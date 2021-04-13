package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.ui.applyIO
import com.zwstudio.lolly.ui.retrofitHtml
import com.zwstudio.lolly.restapi.misc.RestHtml
import io.reactivex.rxjava3.core.Observable

class HtmlService {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    fun getHtml(url: String): Observable<String> =
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
            .applyIO()
}
