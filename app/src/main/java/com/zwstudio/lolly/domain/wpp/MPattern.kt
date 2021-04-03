package com.zwstudio.lolly.domain.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MPatterns {

    @SerializedName("records")
    var lst: List<MPattern>? = null
}

class MPattern: Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("LANGID")
    var langid = 0
    @SerializedName("PATTERN")
    var pattern = ""
    @SerializedName("NOTE")
    var note = ""
    @SerializedName("TAGS")
    var tags = ""
    @SerializedName("IDS_MERGE")
    var idsMerge = ""
    @SerializedName("PATTERNS_SPLIT")
    var patternsSplit = ""
}

class MPatternVariation {
    var index = 0
    var variation = ""
}
