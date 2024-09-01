package com.zwstudio.lolly.models.misc

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MTextbooks(
    @SerializedName("records")
    var lst: List<MTextbook>? = null
)

data class MTextbook(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("NAME")
    var textbookname: String = "",
    @SerializedName("UNITS")
    var units: String = "",
    @SerializedName("PARTS")
    var parts: String = "",
    @SerializedName("ONLINE")
    var online: Int = 0,
) : Serializable {
    var lstUnits = listOf<MSelectItem>()
    fun unitstr(unit: Int) = lstUnits.first { it.value == unit }.label
    var lstParts = listOf<MSelectItem>()
    fun partstr(part: Int) = lstParts.first { it.value == part }.label
}
