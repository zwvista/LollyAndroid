package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MTextbookWords {

    @SerializedName("VTEXTBOOKWORDS")
    @Expose
    var lst: List<MTextbookWord>? = null
}

class MTextbookWord: Serializable {

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
    @SerializedName("WORDID")
    @Expose
    var wordid = 0
    @SerializedName("WORD")
    @Expose
    var word = ""
    @SerializedName("NOTE")
    @Expose
    var note: String? = null
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
