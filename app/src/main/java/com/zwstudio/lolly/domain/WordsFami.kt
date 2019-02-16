package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WordsFami {

    @SerializedName("WORDSFAMI")
    @Expose
    var lst: List<WordFami>? = null
}

class WordFami: Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("USERID")
    @Expose
    var userid: Int = 0
    @SerializedName("WORDID")
    @Expose
    var wordid: Int = 0
    @SerializedName("LEVEL")
    @Expose
    var level: Int = 0
}
