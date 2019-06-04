package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MLanguages {

    @SerializedName("records")
    @Expose
    var lst: List<MLanguage>? = null
}

class MLanguage: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("NAME")
    @Expose
    var langname: String? = null
    @SerializedName("VOICE")
    @Expose
    var voice: String? = null
}
