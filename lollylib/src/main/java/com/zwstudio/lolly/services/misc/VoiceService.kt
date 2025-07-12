package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MVoice
import com.zwstudio.lolly.restapi.misc.RestVoice
import io.reactivex.rxjava3.core.Single

class VoiceService {
    private val api = retrofitJson.create(RestVoice::class.java)

    fun getDataByLang(langid: Int): Single<List<MVoice>> =
        api.getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .map { it.lst }
}
