package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MVoice
import com.zwstudio.lolly.restapi.misc.RestVoice
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

class VoiceService {
    fun getDataByLang(langid: Int): Observable<List<MVoice>> =
        retrofitJson.create(RestVoice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .map { it.lst!! }
}
