package com.zwstudio.lolly.service2.misc

import com.zwstudio.lolly.domain.misc.MVoice
import com.zwstudio.lolly.rest2api.misc.Rest2Voice
import com.zwstudio.lolly.restapi.misc.RestVoice
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class VoiceService2: BaseService2() {
    suspend fun getDataByLang(langid: Int): List<MVoice> =
        retrofitJson2.create(Rest2Voice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .lst!!
}
