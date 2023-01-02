package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MWebPages(
    @SerializedName("records")
    var lst: List<MWebPage>? = null
)

data class MWebPage(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("URL")
    var url: String = "",
) : Serializable
