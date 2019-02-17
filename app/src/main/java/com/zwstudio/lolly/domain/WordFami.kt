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
    var id = 0
    @SerializedName("USERID")
    @Expose
    var userid = 0
    @SerializedName("WORDID")
    @Expose
    var wordid = 0
    @SerializedName("LEVEL")
    @Expose
    var level = 0
}
