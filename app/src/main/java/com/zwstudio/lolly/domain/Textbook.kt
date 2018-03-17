package com.zwstudio.lolly.domain

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Textbooks {

    @SerializedName("TEXTBOOKS")
    @Expose
    var lst: List<Textbook>? = null
}

class Textbook {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("LANGID")
    @Expose
    var langid: Int = 0
    @SerializedName("NAME")
    @Expose
    var textbookname: String? = null
    @SerializedName("UNITS")
    @Expose
    var units: Int = 0
    @SerializedName("PARTS")
    @Expose
    var parts: String? = null
}
