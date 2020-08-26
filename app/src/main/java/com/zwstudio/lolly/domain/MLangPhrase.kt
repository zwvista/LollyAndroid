package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MLangPhrases {

    @SerializedName("records")
    @Expose
    var lst: List<MLangPhrase>? = null
}

class MLangPhrase(): Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("PHRASE")
    @Expose
    var phrase = ""
    @SerializedName("TRANSLATION")
    @Expose
    var translation: String? = null

    constructor(phraseid: Int, langid: Int, phrase: String, translation: String?) : this() {
        this.id = phraseid
        this.langid = langid
        this.phrase = phrase
        this.translation = translation
    }
}
