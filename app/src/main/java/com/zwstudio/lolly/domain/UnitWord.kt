package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnitWords {

    @SerializedName("VUNITWORDS")
    @Expose
    var lst: List<UnitWord>? = null
}

class UnitWord {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("TEXTBOOKID")
    @Expose
    var textbookid: Int = 0
    @SerializedName("UNIT")
    @Expose
    var unit: Int = 0
    @SerializedName("PART")
    @Expose
    var part: Int = 0
    @SerializedName("SEQNUM")
    @Expose
    var seqnum: Int = 0
    @SerializedName("WORD")
    @Expose
    var word: String? = null
    @SerializedName("NOTE")
    @Expose
    var note: String? = null
    @SerializedName("UNITPART")
    @Expose
    var unipart: Int = 0
}