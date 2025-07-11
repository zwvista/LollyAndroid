package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MUserSettings(
    @SerializedName("records")
    var lst: List<MUserSetting> = emptyList()
)

data class MUserSetting(
    @Expose(serialize = false, deserialize = true)
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("USERID")
    var userid: String = "",
    @SerializedName("KIND")
    var kind: Int = 0,
    @SerializedName("ENTITYID")
    var entityid: Int = 0,
    @SerializedName("VALUE1")
    var value1: String? = null,
    @SerializedName("VALUE2")
    var value2: String? = null,
    @SerializedName("VALUE3")
    var value3: String? = null,
    @SerializedName("VALUE4")
    var value4: String? = null,
) : Serializable

class MUserSettingInfo(var usersettingid: Int = 0, var valueid: Int = 0)
