package com.zwstudio.lolly.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MUSMappings {

    @SerializedName("records")
    @Expose
    var lst: List<MUSMapping>? = null
}

class MUSMapping: Serializable {

    @SerializedName("ID")
    @Expose
    var id = 0
    @SerializedName("NAME")
    @Expose
    var name = ""
    @SerializedName("KIND")
    @Expose
    var kind = 0
    @SerializedName("ENTITYID")
    @Expose
    var entityid = 0
    @SerializedName("VALUEID")
    @Expose
    var valueid = 0
    @SerializedName("LEVEL")
    @Expose
    var level = 0

    companion object Companion {

        val NAME_USLANGID = "USLANGID"
        val NAME_USROWSPERPAGEOPTIONS = "USROWSPERPAGEOPTIONS"
        val NAME_USROWSPERPAGE = "USROWSPERPAGE"
        val NAME_USLEVELCOLORS = "USLEVELCOLORS"
        val NAME_USSCANINTERVAL = "USSCANINTERVAL"
        val NAME_USREVIEWINTERVAL = "USREVIEWINTERVAL"

        val NAME_USTEXTBOOKID = "USTEXTBOOKID"
        val NAME_USDICTREFERENCE = "USDICTREFERENCE"
        val NAME_USDICTNOTE = "USDICTNOTE"
        val NAME_USDICTSREFERENCE = "USDICTSREFERENCE"
        val NAME_USDICTTRANSLATION = "USDICTTRANSLATION"
        val NAME_USMACVOICEID = "USMACVOICEID"
        val NAME_USIOSVOICEID = "USIOSVOICEID"
        val NAME_USANDROIDVOICEID = "USANDROIDVOICEID"
        val NAME_USWEBVOICEID = "USWEBVOICEID"
        val NAME_USWINDOWSVOICEID = "USWINDOWSVOICEID"

        val NAME_USUNITFROM = "USUNITFROM"
        val NAME_USPARTFROM = "USPARTFROM"
        val NAME_USUNITTO = "USUNITTO"
        val NAME_USPARTTO = "USPARTTO"
    }
}

