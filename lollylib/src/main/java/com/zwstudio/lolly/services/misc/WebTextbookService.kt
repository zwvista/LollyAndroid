package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MWebTextbook
import com.zwstudio.lolly.restapi.misc.RestWebTextbook
import io.reactivex.rxjava3.core.Single

class WebTextbookService {
    fun getDataByLang(langid: Int): Single<List<MWebTextbook>> =
        retrofitJson.create(RestWebTextbook::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }
}
