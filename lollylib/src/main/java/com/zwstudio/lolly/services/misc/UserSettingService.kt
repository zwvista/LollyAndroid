package com.zwstudio.lolly.services.misc

import android.util.Log
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
        api.getDataByUser("USERID,eq,${GlobalUserViewModel.userid}")
            .lst!!
    }

    suspend fun update(info: MUserSettingInfo, v: Int): Int = withContext(Dispatchers.IO) {
        update(info, v.toString())
    }

    suspend fun update(info: MUserSettingInfo, v: String): Int = withContext(Dispatchers.IO) {
        when (info.valueid) {
            1 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue1(info.usersettingid, v)
                .also { Log.d("API Result", it.toString()) }
            2 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue2(info.usersettingid, v)
                .also { Log.d("API Result", it.toString()) }
            3 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue3(info.usersettingid, v)
                .also { Log.d("API Result", it.toString()) }
            4 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue4(info.usersettingid, v)
                .also { Log.d("API Result", it.toString()) }
            else -> 0
        }
    }
}
