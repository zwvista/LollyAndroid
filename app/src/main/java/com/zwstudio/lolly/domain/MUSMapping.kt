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

    companion object Object {

        val NAME_USLANGID = "USLANGID"
        val NAME_USROWSPERPAGEOPTIONS = "USROWSPERPAGEOPTIONS"
        val NAME_USROWSPERPAGE = "USROWSPERPAGE"
        val NAME_USLEVELCOLORS = "USLEVELCOLORS"
        val NAME_USSCANINTERVAL = "USSCANINTERVAL"
        val NAME_USREVIEWINTERVAL = "USREVIEWINTERVAL"
    }
}

