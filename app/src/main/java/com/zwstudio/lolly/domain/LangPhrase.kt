package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LangPhrases {

    @SerializedName("LANGPHRASES")
    @Expose
    var lst: List<LangPhrase>? = null
}

class LangPhrase: Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("LANGID")
    @Expose
    var langid: Int = 0
    @SerializedName("PHRASE")
    @Expose
    var phrase: String? = null
    @SerializedName("TRANSLATION")
    @Expose
    var translation: String? = null
}
