package com.zwstudio.lolly.service.misc

import android.util.Log
import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.data.misc.Global
import com.zwstudio.lolly.domain.misc.MUserSetting
import com.zwstudio.lolly.domain.misc.MUserSettingInfo
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import io.reactivex.rxjava3.core.Observable

class UserSettingService {
    fun getData(): Observable<List<MUserSetting>> =
        retrofitJson.create(RestUserSetting::class.java)
            .getDataByUser("USERID,eq,${Global.userid}")
            .map { it.lst!! }

    fun update(info: MUserSettingInfo, v: Int): Observable<Int> =
        update(info, v.toString())

    fun update(info: MUserSettingInfo, v: String): Observable<Int> =
        when (info.valueid) {
            1 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue1(info.usersettingid, v)
                .map { Log.d("API Result", it.toString()) }
            2 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue2(info.usersettingid, v)
                .map { Log.d("API Result", it.toString()) }
            3 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue3(info.usersettingid, v)
                .map { Log.d("API Result", it.toString()) }
            4 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue4(info.usersettingid, v)
                .map { Log.d("API Result", it.toString()) }
            else -> Observable.empty()
        }
}
