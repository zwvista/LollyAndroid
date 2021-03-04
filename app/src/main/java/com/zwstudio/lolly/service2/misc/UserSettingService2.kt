package com.zwstudio.lolly.service2.misc

import android.util.Log
import com.zwstudio.lolly.domain.misc.MUserSetting
import com.zwstudio.lolly.domain.misc.MUserSettingInfo
import com.zwstudio.lolly.rest2api.misc.Rest2UserSetting
import com.zwstudio.lolly.restapi.misc.RestUserSetting
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class UserSettingService2: BaseService2() {
    suspend fun getDataByUser(userid: Int): List<MUserSetting> =
        retrofitJson2.create(Rest2UserSetting::class.java)
            .getDataByUser("USERID,eq,$userid")
            .lst!!

    suspend fun update(info: MUserSettingInfo, v: Int): Int =
        update(info, v.toString())

    suspend fun update(info: MUserSettingInfo, v: String): Int =
        when (info.valueid) {
            1 -> retrofitJson2.create(Rest2UserSetting::class.java)
                .updateValue1(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            2 -> retrofitJson2.create(Rest2UserSetting::class.java)
                .updateValue2(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            3 -> retrofitJson2.create(Rest2UserSetting::class.java)
                .updateValue3(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            4 -> retrofitJson2.create(Rest2UserSetting::class.java)
                .updateValue4(info.usersettingid, v)
                .also { Log.d("", it.toString()) }
            else -> 0
        }
}
