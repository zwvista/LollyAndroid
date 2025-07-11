package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MVoices(
    @SerializedName("records")
    var lst: List<MVoice> = emptyList()
)

data class MVoice(
    @Expose(serialize = false, deserialize = true)
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("VOICETYPEID")
    var voicetypeid: Int = 0,
    @SerializedName("VOICELANG")
    var voicelang: String = "",
    @SerializedName("VOICENAME")
    var voicename: String = "",
) : Serializable
