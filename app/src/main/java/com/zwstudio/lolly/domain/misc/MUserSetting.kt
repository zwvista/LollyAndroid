package com.zwstudio.lolly.domain.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUserSettings {

    @SerializedName("records")
    var lst: List<MUserSetting>? = null
}

class MUserSetting: Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("USERID")
    var userid = 0
    @SerializedName("KIND")
    var kind = 0
    @SerializedName("ENTITYID")
    var entityid = 0
    @SerializedName("VALUE1")
    var value1: String? = null
    @SerializedName("VALUE2")
    var value2: String? = null
    @SerializedName("VALUE3")
    var value3: String? = null
    @SerializedName("VALUE4")
    var value4: String? = null
}

class MUserSettingInfo(var usersettingid: Int = 0, var valueid: Int = 0)
