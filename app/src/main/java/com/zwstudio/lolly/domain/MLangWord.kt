package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MLangWords {

    @SerializedName("VLANGWORDS")
    @Expose
    var lst: List<MLangWord>? = null
}

class MLangWord: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("LANGID")
    @Expose
    var langid = 0
    @SerializedName("WORD")
    @Expose
    var word = ""
    @SerializedName("NOTE")
    @Expose
    var note: String? = null
    @SerializedName("FAMIID")
    @Expose
    var famiid = 0
    @SerializedName("LEVEL")
    @Expose
    var level = 0

    fun combineNote(note2: String?): Boolean {
        val oldNote = note
        if (!note2.isNullOrEmpty())
            if (note.isNullOrEmpty())
                note = note2
            else {
                val lst = note!!.split(',').toMutableList()
                if (!lst.contains(note2)) {
                    lst.add(note2)
                    note = lst.joinToString(",")
                }
            }
        return oldNote != note
    }

    val wordnote: String
        get() = word + (if (note.isNullOrEmpty()) "" else "($note)")
}