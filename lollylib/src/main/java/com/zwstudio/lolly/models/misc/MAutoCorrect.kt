package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MAutoCorrects(
    @SerializedName("records")
    var lst: List<MAutoCorrect> = emptyList()
)

data class MAutoCorrect(
    @Expose(serialize = false, deserialize = true)
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("SEQNUM")
    var seqnum: Int = 0,
    @SerializedName("INPUT")
    var input: String = "",
    @SerializedName("EXTENDED")
    var extended: String = "",
    @SerializedName("BASIC")
    var basic: String = "",
) : Serializable

fun autoCorrect(text: String, lstAutoCorrects: List<MAutoCorrect>,
                colFunc1: (MAutoCorrect) -> String, colFunc2: (MAutoCorrect) -> String) =
    lstAutoCorrects.fold(text) { str, row -> str.replace(colFunc1(row), colFunc2(row)) }
