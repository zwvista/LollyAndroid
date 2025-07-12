package com.zwstudio.lolly.services.misc

import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.misc.MSelectItem
import com.zwstudio.lolly.models.misc.MTextbook
import com.zwstudio.suspendapi.restapi.misc.RestTextbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TextbookService {
    private val api = retrofitJson.create(RestTextbook::class.java)

    suspend fun getDataByLang(langid: Int): List<MTextbook> = withContext(Dispatchers.IO) {
        fun f(unitsString: String): List<String> {
            var m = Regex("""UNITS,(\d+)""").find(unitsString)
            if (m != null) {
                val units = m.groupValues[1].toInt()
                return (1..units).map { it.toString() }
            }
            m = Regex("""PAGES,(\d+),(\d+)""").find(unitsString)
            if (m != null) {
                val n1 = m.groupValues[1].toInt()
                val n2 = m.groupValues[2].toInt()
                val units = (n1 + n2 - 1) / n2
                return (1..units).map { "${it * n2 - n2 + 1}~${it * n2}" }
            }
            m = Regex("CUSTOM,(.+)").find(unitsString)
            if (m != null)
                return m.groupValues[1].split(",")
            return listOf()
        }
        api.getDataByLang("LANGID,eq,$langid")
            .lst.also {
                for (o in it) {
                    o.lstUnits = f(o.units).mapIndexed { index, s -> MSelectItem(index + 1, s) }
                    o.lstParts = o.parts.split(",").mapIndexed { index, s -> MSelectItem(index + 1, s) }
                }
            }
    }

}
