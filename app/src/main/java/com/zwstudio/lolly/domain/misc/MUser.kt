package com.zwstudio.lolly.domain.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUsers {

    @SerializedName("records")
    var lst: List<MUser>? = null
}

class MUser : Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("USERID")
    var userid = ""
    @SerializedName("USERNAME")
    var username = ""
    @SerializedName("PASSWORD")
    var password = ""
}
