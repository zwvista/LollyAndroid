package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Languages {

    @SerializedName("LANGUAGES")
    @Expose
    var lst: List<Language>? = null
}

class Language: Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("NAME")
    @Expose
    var langname: String? = null
    @SerializedName("VOICE")
    @Expose
    var voice: String? = null
}
