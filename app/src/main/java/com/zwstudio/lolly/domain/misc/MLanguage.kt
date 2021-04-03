package com.zwstudio.lolly.domain.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MLanguages {

    @SerializedName("records")
    var lst: List<MLanguage>? = null
}

class MLanguage : Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("NAME")
    var langname = ""
}
