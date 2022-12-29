package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MLangPhrases {

    @SerializedName("records")
    var lst: List<MLangPhrase>? = null
}

class MLangPhrase : Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("LANGID")
    var langid = 0
    @SerializedName("PHRASE")
    var phrase = ""
    @SerializedName("TRANSLATION")
    var translation = ""
}
