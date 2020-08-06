package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MVoice
import com.zwstudio.lolly.restapi.RestVoice
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class VoiceService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MVoice>> =
        retrofitJson.create(RestVoice::class.java)
            .getDataByLang("LANGID,eq,$langid", "VOICETYPEID,eq,4")
            .map { it.lst!! }
}
