package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LangWords {

    @SerializedName("LANGWORDS")
    @Expose
    var lst: List<LangWord>? = null
}

class LangWord {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("LANGID")
    @Expose
    var langid: Int = 0
    @SerializedName("WORD")
    @Expose
    var word: String? = null
    @SerializedName("LEVEL")
    @Expose
    var level: Int = 0
}
