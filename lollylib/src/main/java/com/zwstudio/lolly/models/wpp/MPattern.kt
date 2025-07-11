package com.zwstudio.lolly.models.wpp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class MPatterns(
    @SerializedName("records")
    var lst: List<MPattern> = emptyList()
)

@Parcelize
data class MPattern(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("PATTERN")
    var pattern: String = "",
    @SerializedName("TAGS")
    var tags: String = "",
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("URL")
    var url: String = "",
) : Serializable, Parcelable

class MPatternVariation {
    var index = 0
    var variation = ""
}
