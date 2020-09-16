package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.restapi.misc.RestHtml
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class HtmlService: BaseService() {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    fun getHtml(url: String): Observable<String> =
        retrofitHtml.create(RestHtml::class.java)
            .getStringResponse(url)
            .applyIO()
}
