package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MVoice
import com.zwstudio.lolly.restapi.misc.RestVoice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.EBean

class VoiceService {
    suspend fun getDataByLang(langid: Int): List<MVoice> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestVoice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .lst!!
    }
}
