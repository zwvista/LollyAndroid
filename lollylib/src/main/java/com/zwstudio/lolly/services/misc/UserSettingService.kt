package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.completeUpdate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MUserSetting
import com.zwstudio.lolly.models.misc.MUserSettingInfo
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class UserSettingService {
    private val api = retrofitJson.create(RestUserSetting::class.java)

    fun getData(): Single<List<MUserSetting>> =
        api.getDataByUser("USERID,eq,${GlobalUserViewModel.userid}")
            .map { it.lst }

    fun update(info: MUserSettingInfo, v: Int): Completable =
        update(info, v.toString())

    fun update(info: MUserSettingInfo, v: String): Completable =
        (when (info.valueid) {
            1 -> api.updateValue1(info.usersettingid, v)
            2 -> api.updateValue2(info.usersettingid, v)
            3 -> api.updateValue3(info.usersettingid, v)
            4 -> api.updateValue4(info.usersettingid, v)
            else -> Single.just(0)
        }).completeUpdate(info.usersettingid)
}
