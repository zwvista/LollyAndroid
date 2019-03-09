package com.zwstudio.lolly.domain

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MTextbooks {

    @SerializedName("TEXTBOOKS")
    @Expose
    var lst: List<MTextbook>? = null
}

class MTextbook: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("NAME")
    @Expose
    var textbookname: String? = null
    @SerializedName("UNITS")
    @Expose
    var units = ""
    @SerializedName("PARTS")
    @Expose
    var parts = ""

    lateinit var lstUnits: List<MSelectItem>
    lateinit var lstParts: List<MSelectItem>
}