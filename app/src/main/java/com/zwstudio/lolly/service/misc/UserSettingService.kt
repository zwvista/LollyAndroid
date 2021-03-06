package com.zwstudio.lolly.service.misc

import android.util.Log
import com.zwstudio.lolly.domain.misc.MUserSetting
import com.zwstudio.lolly.domain.misc.MUserSettingInfo
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import org.androidannotations.annotations.EBean

@EBean
class UserSettingService: BaseService() {
    suspend fun getDataByUser(userid: Int): List<MUserSetting> =
        retrofitJson2.create(RestUserSetting::class.java)
            .getDataByUser("USERID,eq,$userid")
            .lst!!

    suspend fun update(info: MUserSettingInfo, v: Int): Int =
        update(info, v.toString())

    suspend fun update(info: MUserSettingInfo, v: String): Int =
        when (info.valueid) {
            1 -> retrofitJson2.create(RestUserSetting::class.java)
                .updateValue1(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            2 -> retrofitJson2.create(RestUserSetting::class.java)
                .updateValue2(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            3 -> retrofitJson2.create(RestUserSetting::class.java)
                .updateValue3(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            4 -> retrofitJson2.create(RestUserSetting::class.java)
                .updateValue4(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            else -> 0
        }
}
