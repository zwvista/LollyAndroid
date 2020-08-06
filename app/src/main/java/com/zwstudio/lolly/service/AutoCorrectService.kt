package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MAutoCorrect
import com.zwstudio.lolly.restapi.RestAutoCorrect
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class AutoCorrectService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MAutoCorrect>> =
        retrofitJson.create(RestAutoCorrect::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .map { it.lst!! }
}
