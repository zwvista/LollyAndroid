package com.zwstudio.lolly.models.blogs

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class MLangBlogGroups(
    @SerializedName("records")
    var lst: List<MLangBlogGroup> = emptyList()
)

@Parcelize
data class MLangBlogGroup(
    @SerializedName("ID")
    @Transient var id: Int = 0,
    @SerializedName("LANGID")
    var langid: Int = 0,
    @SerializedName("NAME")
    var groupname: String = ""
) : Serializable, Parcelable {
    @IgnoredOnParcel
    @Transient var gpid = 0
}
