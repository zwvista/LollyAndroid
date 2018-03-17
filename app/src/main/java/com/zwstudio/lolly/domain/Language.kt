package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Languages {

    @SerializedName("LANGUAGES")
    @Expose
    var lst: List<Language>? = null
}

class Language {

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
