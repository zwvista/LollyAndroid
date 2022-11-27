package com.zwstudio.lolly.models.misc

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.zwstudio.lolly.viewmodels.misc.extractTextFrom
import java.io.Serializable
import java.net.URLEncoder

class MDictionaries {

    @SerializedName("records")
    var lst: List<MDictionary>? = null
}

class MDictionary : Serializable {

    @SerializedName("ID")
    var id = 0
    @SerializedName("DICTID")
    var dictid = 0
    @SerializedName("LANGIDFROM")
    var langidfrom = 0
    @SerializedName("LANGNAMEFROM")
    var langnamefrom = ""
    @SerializedName("LANGIDTO")
    var langidto = 0
    @SerializedName("LANGNAMETO")
    var langnameto = ""
    @SerializedName("SEQNUM")
    var seqnum = 0
    @SerializedName("DICTTYPECODE")
    var dicttypecode = 0
    @SerializedName("DICTTYPENAME")
    var dicttypename = ""
    @SerializedName("NAME")
    var dictname = ""
    @SerializedName("URL")
    var url = ""
    @SerializedName("CHCONV")
    var chconv = ""
    @SerializedName("AUTOMATION")
    var automation = ""
    @SerializedName("TRANSFORM")
    var transform = ""
    @SerializedName("WAIT")
    var wait = 0
    @SerializedName("TEMPLATE")
    var template = ""
    @SerializedName("TEMPLATE2")
    var template2 = ""

    fun urlString(word: String, lstAutoCorrects: List<MAutoCorrect>): String {
        val word2 =
            if (chconv == "BASIC")
                autoCorrect(word, lstAutoCorrects, { it.extended }, { it.basic })
            else
                word
        val wordUrl = url.replace("{0}", URLEncoder.encode(word2, "UTF-8"))
        Log.d("urlString", "urlString: " + wordUrl)
        return wordUrl
    }

    fun htmlString(html: String, word: String, useTemplate2: Boolean): String {
        val t = if (useTemplate2 && !template2.isEmpty()) template2 else template
        return extractTextFrom(html, transform, t) { text, t ->
            t.replace( "{0}", word)
                .replace("{1}", cssFolder)
                .replace("{2}", text)
        }
    }
}

val cssFolder = "https://zwvista.com/lolly/css/"
