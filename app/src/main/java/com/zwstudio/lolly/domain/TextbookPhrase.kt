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
    var textbookid: Int = 0
    @SerializedName("LANGID")
    @Expose
    var langid: Int = 0
    @SerializedName("TEXTBOOKNAME")
    @Expose
    var textbookname: String? = null
    @SerializedName("ENTRYID")
    @Expose
    var entryid: Int = 0
    @SerializedName("UNIT")
    @Expose
    var unit: Int = 0
    @SerializedName("PART")
    @Expose
    var part: Int = 0
    @SerializedName("SEQNUM")
    @Expose
    var seqnum: Int = 0
    @SerializedName("PHRASEID")
    @Expose
    var phraseid: Int = 0
    @SerializedName("PHRASE")
    @Expose
    var phrase = ""
    @SerializedName("TRANSLATION")
    @Expose
    var translation: String? = null
    @SerializedName("UNITS")
    @Expose
    var units: Int = 0
    @SerializedName("PARTS")
    @Expose
    var parts = ""

    val lstUnits: List<String>
        get() = (1..units).map { it.toString() }
    val lstParts: List<String>
        get() = parts.split(' ')

    val unitpartseqnum: String
        get() = "$unit $seqnum\n${lstParts[part - 1]}"
}
