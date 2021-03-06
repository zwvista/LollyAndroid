package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.domain.misc.MVoice
import com.zwstudio.lolly.restapi.misc.RestVoice
import org.androidannotations.annotations.EBean

@EBean
class VoiceService: BaseService() {
    suspend fun getDataByLang(langid: Int): List<MVoice> =
        retrofitJson.create(RestVoice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .lst!!
}
