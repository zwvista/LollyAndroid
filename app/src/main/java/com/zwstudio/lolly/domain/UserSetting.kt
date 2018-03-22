package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserSettings {

    @SerializedName("USERSETTINGS")
    @Expose
    var lst: List<UserSetting>? = null
}

class UserSetting: Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("USERID")
    @Expose
    var userid: Int = 0
    @SerializedName("KIND")
    @Expose
    var kind: Int = 0
    @SerializedName("ENTITYID")
    @Expose
    var entityid: Int = 0
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
