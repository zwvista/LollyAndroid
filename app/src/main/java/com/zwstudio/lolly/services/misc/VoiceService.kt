package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.models.misc.MVoice
import com.zwstudio.lolly.restapi.misc.RestVoice
import com.zwstudio.lolly.views.retrofitJson
import io.reactivex.rxjava3.core.Observable

class VoiceService {
    fun getDataByLang(langid: Int): Observable<List<MVoice>> =
        retrofitJson.create(RestVoice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .map { it.lst!! }
}
