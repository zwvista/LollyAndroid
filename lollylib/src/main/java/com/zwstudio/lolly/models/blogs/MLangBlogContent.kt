package com.zwstudio.lolly.models.blogs

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MLangBlogsContent(
    @SerializedName("records")
    var lst: List<MLangBlogPostContent> = emptyList()
)

data class MLangBlogPostContent(
    @SerializedName("ID")
    @Transient var id: Int = 0,
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("CONTENT")
    var content: String = ""
) : Serializable
