package com.zwstudio.lolly.models.blogs

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUnitBlogPosts(
    @SerializedName("records")
    var lst: List<MUnitBlogPost>? = null
)

data class MUnitBlogPost(
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("TEXTBOOKID")
    var textbookid: Int = 0,
    @SerializedName("UNIT")
    var unit: Int = 0,
    @SerializedName("CONTENT")
    var content: String = "",
) : Serializable
