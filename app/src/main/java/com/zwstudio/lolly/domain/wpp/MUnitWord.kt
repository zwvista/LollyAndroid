package com.zwstudio.lolly.domain.wpp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.domain.misc.MTextbook
import java.io.Serializable
import kotlin.math.floor

class MUnitWords {

    @SerializedName("records")
    @Expose
    var lst: List<MUnitWord>? = null
}

class MUnitWord: Serializable {

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
    @SerializedName("WORD")
    @Expose
    var word = ""
    @SerializedName("NOTE")
    @Expose
    var note = ""
    @SerializedName("WORDID")
    @Expose
    var wordid = 0
    @SerializedName("FAMIID")
    @Expose
    var famiid = 0
    @SerializedName("CORRECT")
    @Expose
    var correct = 0
    @SerializedName("TOTAL")
    @Expose
    var total = 0

    lateinit var textbook: MTextbook
    val unitstr: String
        get() = textbook.unitstr(unit)
    val partstr: String
        get() = textbook.partstr(part)
    val unitpartseqnum: String
        get() = "$unitstr\n$partstr\n$seqnum"
    val wordnote: String
        get() = "word($note)"
    val accuracy: String
        get() = if (total == 0) "N/A" else "${floor(correct.toDouble() / total.toDouble() * 1000) / 10}%"
}
