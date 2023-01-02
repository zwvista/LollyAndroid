package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MLangPhrases(
    @SerializedName("records")
    var lst: List<MLangPhrase>? = null
)

data class MLangPhrase(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("PHRASE")
    var phrase: String = "",
    @SerializedName("TRANSLATION")
    var translation: String = "",
) : Serializable
