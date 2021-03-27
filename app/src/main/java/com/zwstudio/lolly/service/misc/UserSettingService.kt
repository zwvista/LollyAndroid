package com.zwstudio.lolly.service.misc

import android.util.Log
import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MUserSetting
import com.zwstudio.lolly.domain.misc.MUserSettingInfo
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserSettingService {
    suspend fun getDataByUser(userid: Int): List<MUserSetting> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUserSetting::class.java)
            .getDataByUser("USERID,eq,$userid")
            .lst!!
    }

    suspend fun update(info: MUserSettingInfo, v: Int): Int = withContext(Dispatchers.IO) {
        update(info, v.toString())
    }

    suspend fun update(info: MUserSettingInfo, v: String): Int = withContext(Dispatchers.IO) {
        when (info.valueid) {
            1 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue1(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            2 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue2(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            3 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue3(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            4 -> retrofitJson.create(RestUserSetting::class.java)
                .updateValue4(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            else -> 0
        }
    }
}
