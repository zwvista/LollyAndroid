package com.zwstudio.lolly.service2.misc

import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.rest2api.misc.Rest2Html
import com.zwstudio.lolly.restapi.misc.RestHtml
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class HtmlService2: BaseService2() {

    // https://futurestud.io/tutorials/retrofit-2-receive-plain-string-responses
    suspend fun getHtml(url: String): String =
        retrofitHtml2.create(Rest2Html::class.java)
            .getStringResponse(url)
}
