package com.zwstudio.lolly.services.misc

import android.util.Log
import com.zwstudio.lolly.models.misc.MUserSetting
import com.zwstudio.lolly.models.misc.MUserSettingInfo
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import com.zwstudio.lolly.viewmodels.misc.Global
import com.zwstudio.lolly.views.retrofitJson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserSettingService {
    fun getData(): Single<List<MUserSetting>> =
        retrofitJson.create(RestUserSetting::class.java)
            .getDataByUser("USERID,eq,${Global.userid}")
            .map { it.lst!! }

    fun update(info: MUserSettingInfo, v: Int): Completable =
        update(info, v.toString())

    fun update(info: MUserSettingInfo, v: String): Completable =
        (when (info.valueid) {
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
            else -> Single.just(0)
        }).flatMapCompletable { Completable.complete() }
}
