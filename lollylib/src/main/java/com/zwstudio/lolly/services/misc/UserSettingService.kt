package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MUserSetting
import com.zwstudio.lolly.models.misc.MUserSettingInfo
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserSettingService {
    private val api = retrofitJson.create(RestUserSetting::class.java)

    suspend fun getData(): List<MUserSetting> = withContext(Dispatchers.IO) {
        api.getDataByUser("USERID,eq,${GlobalUserViewModel.userid}").lst
    }

    suspend fun update(info: MUserSettingInfo, v: Int) = withContext(Dispatchers.IO) {
        update(info, v.toString())
    }

    suspend fun update(info: MUserSettingInfo, v: String) = withContext(Dispatchers.IO) {
        (when (info.valueid) {
            1 -> api.updateValue1(info.usersettingid, v)
            2 -> api.updateValue2(info.usersettingid, v)
            3 -> api.updateValue3(info.usersettingid, v)
            4 -> api.updateValue4(info.usersettingid, v)
            else -> 0
        }).logUpdate(info.usersettingid)
    }
}
