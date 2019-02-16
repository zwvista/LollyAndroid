package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TextbookPhrases {

    @SerializedName("VTEXTBOOKPHRASES")
    @Expose
    var lst: List<TextbookPhrase>? = null
}

class TextbookPhrase: Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("LANGID")
    @Expose
    var langid: Int = 0
    @SerializedName("TEXTBOOKNAME")
    @Expose
    var textbookname: String? = null
    @SerializedName("UNIT")
    @Expose
    var unit: Int = 0
    @SerializedName("PART")
    @Expose
    var part: Int = 0
    @SerializedName("SEQNUM")
    @Expose
    var seqnum: Int = 0
    @SerializedName("PHRASE")
    @Expose
    var phrase: String? = null
    @SerializedName("TRANSLATION")
    @Expose
    var translation: String? = null
}
