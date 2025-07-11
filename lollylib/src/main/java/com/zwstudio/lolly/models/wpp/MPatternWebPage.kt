package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MPatternWebPages(
    @SerializedName("records")
    var lst: List<MPatternWebPage> = emptyList()
)

data class MPatternWebPage(
    @Expose(serialize = false, deserialize = true)
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("PATTERNID")
    var patternid: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("PATTERN")
    var pattern: String = "",
    @SerializedName("WEBPAGEID")
    var webpageid: Int = 0,
    @SerializedName("SEQNUM")
    var seqnum: Int = 0,
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("URL")
    var url: String = "",
) : Serializable
