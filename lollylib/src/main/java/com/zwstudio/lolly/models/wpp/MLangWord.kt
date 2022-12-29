package com.zwstudio.lolly.models.wpp

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.math.floor

data class MLangWords(
    @SerializedName("records")
    var lst: List<MLangWord>? = null
)

data class MLangWord(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("WORD")
    var word: String = "",
    @SerializedName("NOTE")
    var note: String = "",
    @SerializedName("FAMIID")
    var famiid: Int = 0,
    @SerializedName("CORRECT")
    var correct: Int = 0,
    @SerializedName("TOTAL")
    var total: Int = 0,
) : Serializable {
    val wordnote: String
        get() = "$word($note)"
    val accuracy: String
        get() = if (total == 0) "N/A" else "${floor(correct.toDouble() / total.toDouble() * 1000) / 10}%"
}
