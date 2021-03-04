package com.zwstudio.lolly.service2.misc

import com.zwstudio.lolly.domain.misc.MAutoCorrect
import com.zwstudio.lolly.rest2api.misc.Rest2AutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class AutoCorrectService2: BaseService2() {
    suspend fun getDataByLang(langid: Int): List<MAutoCorrect> =
        retrofitJson2.create(Rest2AutoCorrect::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!
}
