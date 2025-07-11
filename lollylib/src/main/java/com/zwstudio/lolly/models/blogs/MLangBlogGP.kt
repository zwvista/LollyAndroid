package com.zwstudio.lolly.models.blogs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MLangBlogGPs(
    @SerializedName("records")
    var lst: List<MLangBlogGP> = emptyList()
)

data class MLangBlogGP(
    @Expose(serialize = false, deserialize = true)
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("GROUPID")
    var groupid: Int = 0,
    @SerializedName("POSTID")
    var postid: Int = 0,
    @SerializedName("GROUPNAME")
    var groupname: String = "",
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("URL")
    var url: String = ""
) : Serializable
