package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MUsers(
    @SerializedName("records")
    var lst: List<MUser>? = null
)

data class MUser(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("USERID")
    var userid: String = "",
    @SerializedName("USERNAME")
    var username: String = "",
    @SerializedName("PASSWORD")
    var password: String = "",
) : Serializable
