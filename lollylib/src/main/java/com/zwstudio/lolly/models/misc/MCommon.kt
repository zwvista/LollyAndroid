package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MSelectItem(val value: Int, val label: String) : Serializable

data class MSPResult(
    @SerializedName("NEW_ID")
    var newid: String? = null,
    @SerializedName("result")
    var result: String = "",
) : Serializable
