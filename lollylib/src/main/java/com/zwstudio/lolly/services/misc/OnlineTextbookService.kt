package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MOnlineTextbook
import com.zwstudio.lolly.restapi.misc.RestOnlineTextbook
import io.reactivex.rxjava3.core.Single

class OnlineTextbookService {
    private val api = retrofitJson.create(RestOnlineTextbook::class.java)

    fun getDataByLang(langid: Int): Single<List<MOnlineTextbook>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }
}
