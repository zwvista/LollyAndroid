package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.views.retrofitJson
import com.zwstudio.lolly.models.misc.MAutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import io.reactivex.rxjava3.core.Observable

class AutoCorrectService {
    fun getDataByLang(langid: Int): Observable<List<MAutoCorrect>> =
        retrofitJson.create(RestAutoCorrect::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }
}
