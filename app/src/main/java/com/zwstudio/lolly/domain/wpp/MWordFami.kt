package com.zwstudio.lolly.domain.wpp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MWordsFami {

    @SerializedName("records")
    @Expose
    var lst: List<MWordFami>? = null
}

class MWordFami: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("USERID")
    @Expose
    var userid = 0
    @SerializedName("WORDID")
    @Expose
    var wordid = 0
    @SerializedName("CORRECT")
    @Expose
    var correct = 0
    @SerializedName("TOTAL")
    @Expose
    var total = 0
}
