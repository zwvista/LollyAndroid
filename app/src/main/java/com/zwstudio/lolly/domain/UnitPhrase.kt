package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UnitPhrases {

    @SerializedName("VUNITPHRASES")
    @Expose
    var lst: List<UnitPhrase>? = null
}

class UnitPhrase: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("TEXTBOOKID")
    @Expose
    var textbookid = 0
    @SerializedName("UNIT")
    @Expose
    var unit = 0
    @SerializedName("PART")
    @Expose
    var part = 0
    @SerializedName("SEQNUM")
    @Expose
    var seqnum = 0
    @SerializedName("PHRASEID")
    @Expose
    var phraseid = 0
    @SerializedName("PHRASE")
    @Expose
    var phrase = ""
    @SerializedName("TRANSLATION")
    @Expose
    var translation: String? = null

    fun unitpartseqnum(lstParts: List<String>) = "$unit $seqnum\n${lstParts[part - 1]}"
}
