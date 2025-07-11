package com.zwstudio.lolly.models.misc

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MUSMappings(
    @SerializedName("records")
    var lst: List<MUSMapping> = emptyList()
)

data class MUSMapping(
    @Expose(serialize = false, deserialize = true)
    @SerializedName("ID")
    var id: Int = 0,
    @SerializedName("NAME")
    var name: String = "",
    @SerializedName("KIND")
    var kind: Int = 0,
    @SerializedName("ENTITYID")
    var entityid: Int = 0,
    @SerializedName("VALUEID")
    var valueid: Int = 0,
    @SerializedName("LEVEL")
    var level: Int = 0,
) : Serializable {
    companion object Companion {

        const val NAME_USLANG = "USLANG"

        const val NAME_USTEXTBOOK = "USTEXTBOOK"
        const val NAME_USDICTREFERENCE = "USDICTREFERENCE"
        const val NAME_USDICTNOTE = "USDICTNOTE"
        const val NAME_USDICTTRANSLATION = "USDICTTRANSLATION"
        const val NAME_USVOICE = "USANDROIDVOICE"

        const val NAME_USUNITFROM = "USUNITFROM"
        const val NAME_USPARTFROM = "USPARTFROM"
        const val NAME_USUNITTO = "USUNITTO"
        const val NAME_USPARTTO = "USPARTTO"
    }
}

