package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.data.extractTextFrom
import java.io.Serializable
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class MDictsReference {

    @SerializedName("records")
    @Expose
    var lst: List<MDictReference>? = null
}

class MDictsNote {

    @SerializedName("records")
    @Expose
    var lst: List<MDictNote>? = null
}

class MDictsTranslation {

    @SerializedName("records")
    @Expose
    var lst: List<MDictTranslation>? = null
}

abstract class MDictionary: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("DICTID")
    @Expose
    var dictid = 0
    @SerializedName("LANGIDFROM")
    @Expose
    var langidfrom: Int? = null
    @SerializedName("LANGIDTO")
    @Expose
    var langidto: Int? = null
    @SerializedName("SEQNUM")
    @Expose
    var seqnum: Int? = null
    @SerializedName("DICTTYPENAME")
    @Expose
    var dicttypename: String? = null
    @SerializedName("DICTNAME")
    @Expose
    var dictname: String? = null
    @SerializedName("URL")
    @Expose
    var url: String? = null
    @SerializedName("CHCONV")
    @Expose
    var chconv: String? = null
    @SerializedName("AUTOMATION")
    @Expose
    var automation: String? = null
    @SerializedName("AUTOJUMP")
    @Expose
    var autojump: Int? = null
    @SerializedName("DICTTABLE")
    @Expose
    var dicttable: String? = null
    @SerializedName("TRANSFORM")
    @Expose
    var transform: String? = null
    @SerializedName("WAIT")
    @Expose
    var wait: Int? = null
    @SerializedName("TEMPLATE")
    @Expose
    var template: String? = null
    @SerializedName("TEMPLATE2")
    @Expose
    var template2: String? = null

    fun urlString(word: String, lstAutoCorrects: List<MAutoCorrect>): String {
        val word2 =
            if (chconv == "BASIC")
                autoCorrect(word, lstAutoCorrects, { it.extended }, { it.basic })
            else
                word
        var wordUrl: String? = null
        try {
            wordUrl = url!!.replace("{0}", URLEncoder.encode(word2, "UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        Log.d("", "urlString: " + wordUrl!!)
        return wordUrl
    }
}

val cssFolder = "https://zwvista.tk/lolly/css/"

class MDictReference: MDictionary() {
    fun htmlString(html: String, word: String, useTemplate2: Boolean): String {
        val t = if (useTemplate2 && !template2.isNullOrEmpty()) template2!! else template!!
        return extractTextFrom(html, transform!!, t) { text, t ->
            t.replace( "{0}", word)
                .replace("{1}", cssFolder)
                .replace("{2}", text)
        }
    }
}

class MDictItem(val dictid: String, val dictname: String) {
    fun dictids(): List<String> = dictid.split(",")
}

class MDictNote: MDictionary()

class MDictTranslation: MDictionary()
