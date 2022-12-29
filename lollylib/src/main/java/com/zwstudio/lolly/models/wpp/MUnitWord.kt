package com.zwstudio.lolly.models.wpp

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.models.misc.MTextbook
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import kotlin.math.floor

data class MUnitWords(
    @SerializedName("records")
    var lst: List<MUnitWord>? = null
)

@Parcelize
data class MUnitWord(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("TEXTBOOKID")
    var textbookid: Int = 0,
    @SerializedName("TEXTBOOKNAME")
    var textbookname: String = "",
    @SerializedName("UNIT")
    var unit: Int = 0,
    @SerializedName("PART")
    var part: Int = 0,
    @SerializedName("SEQNUM")
    var seqnum: Int = 0,
    @SerializedName("WORD")
    var word: String = "",
    @SerializedName("NOTE")
    var note: String = "",
    @SerializedName("WORDID")
    var wordid: Int = 0,
    @SerializedName("FAMIID")
    var famiid: Int = 0,
    @SerializedName("CORRECT")
    var correct: Int = 0,
    @SerializedName("TOTAL")
    var total: Int = 0,
) : Serializable, Parcelable {
    @IgnoredOnParcel
    @Expose(deserialize = false)
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
