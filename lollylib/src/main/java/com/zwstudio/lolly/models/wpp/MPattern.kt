package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MPatterns(
    @SerializedName("records")
    var lst: List<MPattern>? = null
)

data class MPattern(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("PATTERN")
    var pattern: String = "",
    @SerializedName("NOTE")
    var note: String = "",
    @SerializedName("TAGS")
    var tags: String = "",
    @SerializedName("IDS_MERGE")
    var idsMerge: String = "",
    @SerializedName("PATTERNS_SPLIT")
    var patternsSplit: String = "",
) : Serializable

class MPatternVariation {
    var index = 0
    var variation = ""
}
