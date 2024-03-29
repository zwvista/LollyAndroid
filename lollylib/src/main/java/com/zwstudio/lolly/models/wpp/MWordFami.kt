package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MWordsFami(
    @SerializedName("records")
    var lst: List<MWordFami>? = null
)

data class MWordFami(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("USERID")
    var userid: String = "",
    @SerializedName("WORDID")
    var wordid: Int = 0,
    @SerializedName("CORRECT")
    var correct: Int = 0,
    @SerializedName("TOTAL")
    var total: Int = 0,
) : Serializable
