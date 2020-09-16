package com.zwstudio.lolly.domain.misc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUserSettings {

    @SerializedName("records")
    @Expose
    var lst: List<MUserSetting>? = null
}

class MUserSetting: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("USERID")
    @Expose
    var userid = 0
    @SerializedName("KIND")
    @Expose
    var kind = 0
    @SerializedName("ENTITYID")
    @Expose
    var entityid = 0
    @SerializedName("VALUE1")
    @Expose
    var value1: String? = null
    @SerializedName("VALUE2")
    @Expose
    var value2: String? = null
    @SerializedName("VALUE3")
    @Expose
    var value3: String? = null
    @SerializedName("VALUE4")
    @Expose
    var value4: String? = null
}

class MUserSettingInfo(var usersettingid: Int = 0, var valueid: Int = 0)
