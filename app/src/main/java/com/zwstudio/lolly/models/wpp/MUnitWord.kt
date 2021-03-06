package com.zwstudio.lolly.models.wpp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.models.misc.MTextbook
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import kotlin.math.floor

class MUnitWords {

    @SerializedName("records")
    var lst: List<MUnitWord>? = null
}

@Parcelize
class MUnitWord : Serializable, Parcelable {

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
    @SerializedName("WORD")
    var word = ""
    @SerializedName("NOTE")
    var note = ""
    @SerializedName("WORDID")
    var wordid = 0
    @SerializedName("FAMIID")
    var famiid = 0
    @SerializedName("CORRECT")
    var correct = 0
    @SerializedName("TOTAL")
    var total = 0

    lateinit var textbook: MTextbook
    val unitstr: String
        get() = textbook.unitstr(unit)
    val partstr: String
        get() = textbook.partstr(part)
    val unitpartseqnum: String
        get() = "$unitstr\n$partstr\n$seqnum"
    val wordnote: String
        get() = "$word($note)"
    val accuracy: String
        get() = if (total == 0) "N/A" else "${floor(correct.toDouble() / total.toDouble() * 1000) / 10}%"

    var unitIndex: Int
        get() = textbook.lstUnits.indexOfFirst { it.value == unit }
        set(value) { unit = textbook.lstUnits[value].value }
    var partIndex: Int
        get() = textbook.lstParts.indexOfFirst { it.value == part }
        set(value) { part = textbook.lstParts[value].value }
}
