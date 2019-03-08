package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TextbookPhrases {

    @SerializedName("VTEXTBOOKPHRASES")
    @Expose
    var lst: List<TextbookPhrase>? = null
}

class TextbookPhrase: Serializable {

    @SerializedName("TEXTBOOKID")
    @Expose
    var textbookid = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("TEXTBOOKNAME")
    @Expose
    var textbookname: String? = null
    @SerializedName("ID")
    @Expose
    var id = 0
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

    lateinit var lstUnits: List<SelectItem>
    lateinit var lstParts: List<SelectItem>
    val unitstr: String
        get() = lstUnits.first { it.value == unit }.label
    val partstr: String
        get() = lstParts.first { it.value == part }.label
    val unitpartseqnum: String
        get() = "$unitstr $seqnum\n$partstr"
}
