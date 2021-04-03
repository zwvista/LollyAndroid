package com.zwstudio.lolly.domain.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MVoices {

    @SerializedName("records")
    var lst: List<MVoice>? = null
}

class MVoice: Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("LANGID")
    var langid = 0
    @SerializedName("VOICETYPEID")
    var voicetypeid = 0
    @SerializedName("VOICELANG")
    var voicelang = ""
    @SerializedName("VOICENAME")
    var voicename = ""
}
