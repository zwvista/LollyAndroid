package com.zwstudio.lolly.domain.wpp

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.math.floor

class MLangWords {

    @SerializedName("records")
    var lst: List<MLangWord>? = null
}

class MLangWord: Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("LANGID")
    var langid = 0
    @SerializedName("WORD")
    var word = ""
    @SerializedName("NOTE")
    var note = ""
    @SerializedName("FAMIID")
    var famiid = 0
    @SerializedName("CORRECT")
    var correct = 0
    @SerializedName("TOTAL")
    var total = 0

    val wordnote: String
        get() = "$word($note)"
    val accuracy: String
        get() = if (total == 0) "N/A" else "${floor(correct.toDouble() / total.toDouble() * 1000) / 10}%"
}
