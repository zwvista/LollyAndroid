package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class LangPhrases {

    @SerializedName("LANGPHRASES")
    @Expose
    var lst: List<LangPhrase>? = null
}

class LangPhrase(): Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
    @SerializedName("LANGID")
    @Expose
    var langid: Int = 0
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

    fun combinetranslation(translation2: String?): Boolean {
        val oldTranslation = translation
        if (!translation2.isNullOrEmpty())
            if (translation.isNullOrEmpty())
                translation = translation2
            else {
                val lst = translation!!.split(',').toMutableList()
                if (!lst.contains(translation2)) {
                    lst.add(translation2)
                    translation = lst.joinToString(",")
                }
            }
        return oldTranslation != translation
    }
}
