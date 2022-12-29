package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MVoice
import com.zwstudio.lolly.restapi.misc.RestVoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoiceService {
    suspend fun getDataByLang(langid: Int): List<MVoice> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestVoice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .lst!!
    }
}
