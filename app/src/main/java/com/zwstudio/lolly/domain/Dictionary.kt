package com.zwstudio.lolly.domain

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DictsOnline {

    @SerializedName("VDICTSONLINE")
    @Expose
    var lst: List<DictOnline>? = null
}

class DictsOffline {

    @SerializedName("VDICTSOFFLINE")
    @Expose
    var lst: List<DictOffline>? = null
}

class DictsNote {

    @SerializedName("VDICTSNOTE")
    @Expose
    var lst: List<DictNote>? = null
}

abstract class Dictionary: Serializable {

    @SerializedName("ID")
    @Expose
    var id: Int = 0
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
    @SerializedName("TRANSFORM_WIN")
    @Expose
    var transformWin: String? = null
    @SerializedName("TRANSFORM_MAC")
    @Expose
    var transformMac: String? = null
    @SerializedName("WAIT")
    @Expose
    var wait: Int? = null
    @SerializedName("TEMPLATE")
    @Expose
    var template: String? = null

}

class DictOnline: Dictionary()
class DictOffline: Dictionary()
class DictNote: Dictionary()
