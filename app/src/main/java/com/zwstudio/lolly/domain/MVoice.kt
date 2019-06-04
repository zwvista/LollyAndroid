package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MVoices {

    @SerializedName("records")
    @Expose
    var lst: List<MVoice>? = null
}

class MVoice: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("VOICETYPEID")
    @Expose
    var voicetypeid = 0
    @SerializedName("VOICELANG")
    @Expose
    var voicelang: String? = null
    @SerializedName("VOICENAME")
    @Expose
    var voicename = ""
}
