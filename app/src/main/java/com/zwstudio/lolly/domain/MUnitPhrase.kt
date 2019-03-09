package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUnitPhrases {

    @SerializedName("VUNITPHRASES")
    @Expose
    var lst: List<MUnitPhrase>? = null
}

class MUnitPhrase: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("TEXTBOOKID")
    @Expose
    var textbookid = 0
    @SerializedName("TEXTBOOKNAME")
    @Expose
    var textbookname = ""
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

    lateinit var lstUnits: List<MSelectItem>
    lateinit var lstParts: List<MSelectItem>
    val unitstr: String
        get() = lstUnits.first { it.value == unit }.label
    val partstr: String
        get() = lstParts.first { it.value == part }.label
    val unitpartseqnum: String
        get() = "$unitstr $seqnum\n$partstr"
}
