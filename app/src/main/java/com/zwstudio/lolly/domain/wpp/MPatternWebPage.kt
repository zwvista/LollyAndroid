package com.zwstudio.lolly.domain.wpp

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MPatternWebPages {

    @SerializedName("records")
    var lst: List<MPatternWebPage>? = null
}

class MPatternWebPage : Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("PATTERNID")
    var patternid = 0
    @SerializedName("LANGID")
    var langid = 0
    @SerializedName("PATTERN")
    var pattern = ""
    @SerializedName("WEBPAGEID")
    var webpageid = 0
    @SerializedName("SEQNUM")
    var seqnum = 0
    @SerializedName("TITLE")
    var title = ""
    @SerializedName("URL")
    var url = ""
}
