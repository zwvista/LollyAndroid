package com.zwstudio.lolly.domain

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.data.extractTextFrom
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class MSelectItem(val value: Int, val label: String): Serializable

enum class ReviewMode {
    ReviewAuto, Test, ReviewManual;

    override fun toString(): String {
        return when(this) {
            ReviewAuto -> "Review(Auto)"
            Test -> "Test"
            ReviewManual -> "Review(Manual)"
        }
    }
}

class MSPResult: Serializable {

    @SerializedName("NEW_ID")
    @Expose
    var newid: String? = null
    @SerializedName("result")
    @Expose
    var result = ""
}
