package com.zwstudio.lolly.models.wpp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.models.misc.MTextbook
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable

class MUnitPhrases {
    @SerializedName("records")
    var lst: List<MUnitPhrase>? = null
}

@Parcelize
data class MUnitPhrase(
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
    @SerializedName("PHRASEID")
    var phraseid: Int = 0,
    @SerializedName("PHRASE")
    var phrase: String = "",
    @SerializedName("TRANSLATION")
    var translation: String = "",
) : Serializable, Parcelable {
    @IgnoredOnParcel
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
