package com.zwstudio.lolly.service

import android.util.Log
import com.zwstudio.lolly.domain.MUserSetting
import com.zwstudio.lolly.restapi.RestUserSetting
import io.reactivex.Observable
import org.androidannotations.annotations.EBean

@EBean
class UserSettingService: BaseService() {
    fun getDataByUser(userid: Int): Observable<List<MUserSetting>> =
        retrofitJson.create(RestUserSetting::class.java)
            .getDataByUser("USERID,eq,${userid}")
            .map { it.lst!! }

    fun updateLang(id: Int, langid: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateLang(id, langid)
            .map { Log.d("", it.toString()) }

    fun updateTextbook(id: Int, textbookid: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateTextbook(id, textbookid)
            .map { Log.d("", it.toString()) }

    fun updateDictItem(id: Int, dictitem: String): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDictItem(id, dictitem)
            .map { Log.d("", it.toString()) }

    fun updateDictNote(id: Int, dictnodeid: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateDictNote(id, dictnodeid)
            .map { Log.d("", it.toString()) }

    fun updateUnitFrom(id: Int, unitfrom: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateUnitFrom(id, unitfrom)
            .map { Log.d("", it.toString()) }

    fun updatePartFrom(id: Int, partfrom: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updatePartFrom(id, partfrom)
            .map { Log.d("", it.toString()) }

    fun updateUnitTo(id: Int, unitto: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateUnitTo(id, unitto)
            .map { Log.d("", it.toString()) }

    fun updatePartTo(id: Int, partto: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updatePartTo(id, partto)
            .map { Log.d("", it.toString()) }

    fun updateVoice(id: Int, voiceid: Int): Observable<Int> =
        retrofitJson.create(RestUserSetting::class.java)
            .updateVoice(id, voiceid)
            .map { Log.d("", it.toString()) }
}
