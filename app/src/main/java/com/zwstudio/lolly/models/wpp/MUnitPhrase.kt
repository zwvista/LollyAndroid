package com.zwstudio.lolly.models.wpp

import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.models.misc.MTextbook
import java.io.Serializable

class MUnitPhrases {

    @SerializedName("records")
    var lst: List<MUnitPhrase>? = null
}

class MUnitPhrase : Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("LANGID")
    var langid = 0
    @SerializedName("TEXTBOOKID")
    var textbookid = 0
    @SerializedName("TEXTBOOKNAME")
    var textbookname = ""
    @SerializedName("UNIT")
    var unit = 0
    @SerializedName("PART")
    var part = 0
    @SerializedName("SEQNUM")
    var seqnum = 0
    @SerializedName("PHRASEID")
    var phraseid = 0
    @SerializedName("PHRASE")
    var phrase = ""
    @SerializedName("TRANSLATION")
    var translation = ""

    lateinit var textbook: MTextbook
    val unitstr: String
        get() = textbook.unitstr(unit)
    val partstr: String
        get() = textbook.partstr(part)
    val unitpartseqnum: String
        get() = "$unitstr\n$partstr\n$seqnum"

    var unitIndex: Int
        get() = textbook.lstUnits.indexOfFirst { it.value == unit }
        set(value) { unit = textbook.lstUnits[value].value }
    var partIndex: Int
        get() = textbook.lstParts.indexOfFirst { it.value == part }
        set(value) { part = textbook.lstParts[value].value }
}
