package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UnitPhrases {

    @SerializedName("VUNITPHRASES")
    @Expose
    var lst: List<UnitPhrase>? = null
}

class UnitPhrase {

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
    @SerializedName("LANGPHRASEID")
    @Expose
    var langphraseid: Int? = null
    @SerializedName("PHRASE")
    @Expose
    var phrase: String? = null
    @SerializedName("TRANSLATION")
    @Expose
    var translation: String? = null
    @SerializedName("UNITPART")
    @Expose
    var unipart: Int = 0
}
