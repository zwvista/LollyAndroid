package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.domain.misc.MAutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import org.androidannotations.annotations.EBean

@EBean
class AutoCorrectService: BaseService() {
    suspend fun getDataByLang(langid: Int): List<MAutoCorrect> =
        retrofitJson2.create(RestAutoCorrect::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
}
