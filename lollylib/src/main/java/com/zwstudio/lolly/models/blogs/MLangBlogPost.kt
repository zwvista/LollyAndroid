package com.zwstudio.lolly.models.blogs

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class MLangBlogPosts(
    @SerializedName("records")
    var lst: List<MLangBlogPost> = emptyList()
)

@Parcelize
data class MLangBlogPost(
    @SerializedName("ID")
    @Transient var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("TITLE")
    var title: String = "",
    @SerializedName("URL")
    var url: String = ""
) : Serializable, Parcelable {
    @IgnoredOnParcel
    @Transient var gpid = 0
}
