package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MLanguages(
    @SerializedName("records")
    var lst: List<MLanguage>? = null
)

data class MLanguage(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("NAME")
    var langname: String = "",
) : Serializable
