package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUnitWords {

    @SerializedName("VUNITWORDS")
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
    var note: String? = null
    @SerializedName("WORDID")
    @Expose
    var wordid = 0
    @SerializedName("FAMIID")
    @Expose
    var famiid = 0
    @SerializedName("LEVEL")
    @Expose
    var level = 0

    lateinit var lstUnits: List<MSelectItem>
    lateinit var lstParts: List<MSelectItem>
    val unitstr: String
        get() = lstUnits.first { it.value == unit }.label
    val partstr: String
        get() = lstParts.first { it.value == part }.label
    val unitpartseqnum: String
        get() = "$unitstr $seqnum\n$partstr"
    val wordnote: String
        get() = word + (if (note.isNullOrEmpty()) "" else "($note)")
}
