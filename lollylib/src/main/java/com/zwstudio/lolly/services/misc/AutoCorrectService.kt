package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MAutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import io.reactivex.rxjava3.core.Single

class AutoCorrectService {
    private val api = retrofitJson.create(RestAutoCorrect::class.java)

    fun getDataByLang(langid: Int): Single<List<MAutoCorrect>> =
        api.getDataByLang("LANGID,eq,$langid")
            .map { it.lst }
}
