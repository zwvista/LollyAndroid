package com.zwstudio.lolly.domain

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Textbooks {

    @SerializedName("TEXTBOOKS")
    @Expose
    var lst: List<Textbook>? = null
}

class Textbook: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("NAME")
    @Expose
    var textbookname: String? = null
    @SerializedName("UNITINFO")
    @Expose
    var unitinfo = ""
    @SerializedName("PARTS")
    @Expose
    var parts = ""
}
