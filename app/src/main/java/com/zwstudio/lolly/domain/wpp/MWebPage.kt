package com.zwstudio.lolly.domain.wpp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MWebPages {

    @SerializedName("records")
    @Expose
    var lst: List<MWebPage>? = null
}

class MWebPage: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("TITLE")
    @Expose
    var title = ""
    @SerializedName("URL")
    @Expose
    var url = ""
}
