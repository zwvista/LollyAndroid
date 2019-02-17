package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TextbookWords {

    @SerializedName("VTEXTBOOKWORDS")
    @Expose
    var lst: List<TextbookWord>? = null
}

class TextbookWord: Serializable {

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
    @SerializedName("UNITS")
    @Expose
    var units = 0
    @SerializedName("PARTS")
    @Expose
    var parts = ""

    val lstUnits: List<String>
        get() = (1..units).map { it.toString() }
    val lstParts: List<String>
        get() = parts.split(' ')

    val unitpartseqnum: String
        get() = "$unit $seqnum\n${lstParts[part - 1]}"

    val wordnote: String
        get() = word + (if (note.isNullOrEmpty()) "" else "($note)")
}
