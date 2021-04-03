package com.zwstudio.lolly.domain.misc

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MSelectItem(val value: Int, val label: String): Serializable

class MSPResult: Serializable {

    @SerializedName("NEW_ID")
    var newid: String? = null
    @SerializedName("result")
    var result = ""
}
