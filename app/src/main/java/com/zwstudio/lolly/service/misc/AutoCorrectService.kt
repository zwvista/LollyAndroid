package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MAutoCorrect
import com.zwstudio.lolly.restapi.misc.RestAutoCorrect
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class AutoCorrectService {
    fun getDataByLang(langid: Int): Observable<List<MAutoCorrect>> =
        retrofitJson.create(RestAutoCorrect::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }
}
