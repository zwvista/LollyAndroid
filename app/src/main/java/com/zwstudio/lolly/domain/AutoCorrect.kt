package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AutoCorrects {

    @SerializedName("AUTOCORRECT")
    @Expose
    var lst: List<AutoCorrect>? = null
}

class AutoCorrect: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("SEQNUM")
    @Expose
    var seqnum = 0
    @SerializedName("INPUT")
    @Expose
    var input: String = ""
    @SerializedName("EXTENDED")
    @Expose
    var extended: String = ""
    @SerializedName("BASIC")
    @Expose
    var basic: String = ""
}

fun autoCorrect(text: String, lstAutoCorrects: List<AutoCorrect>,
                colFunc1: (AutoCorrect) -> String, colFunc2: (AutoCorrect) -> String) =
    lstAutoCorrects.fold(text) { str, row -> str.replace(colFunc1(row), colFunc2(row)) }
