package com.zwstudio.lolly.domain.wpp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MPatternWebPages {

    @SerializedName("records")
    @Expose
    var lst: List<MPatternWebPage>? = null
}

class MPatternWebPage: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("PATTERNID")
    @Expose
    var patternid = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("PATTERN")
    @Expose
    var pattern = ""
    @SerializedName("WEBPAGEID")
    @Expose
    var webpageid = 0
    @SerializedName("SEQNUM")
    @Expose
    var seqnum = 0
    @SerializedName("TITLE")
    @Expose
    var title = ""
    @SerializedName("URL")
    @Expose
    var url = ""
}
