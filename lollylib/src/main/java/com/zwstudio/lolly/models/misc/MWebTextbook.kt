package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MWebTextbooks(
    @SerializedName("records")
    var lst: List<MWebTextbook>? = null
)

class MWebTextbook(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("TEXTBOOKID")
    var textbookid: Int = 0,
    @SerializedName("TEXTBOOKNAME")
    var textbookname: String = "",
    @SerializedName("UNIT")
    var unit: Int = 0,
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("URL")
    var url: String = "",
) : Serializable
