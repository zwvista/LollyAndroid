package com.zwstudio.lolly.service.misc

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.misc.MSelectItem
import com.zwstudio.lolly.domain.misc.MTextbook
import com.zwstudio.suspendapi.restapi.misc.RestTextbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TextbookService {
    suspend fun getDataByLang(langid: Int): List<MTextbook> = withContext(Dispatchers.IO) {
        fun f(units: String): List<String> {
            var m = Regex("UNITS,(\\d+)").find(units)
            if (m != null) {
                val units = m.groupValues[1].toInt()
                return (1..units).map { it.toString() }
            }
            m = Regex("PAGES,(\\d+),(\\d+)").find(units)
            if (m != null) {
                val n1 = m.groupValues[1].toInt()
                val n2 = m.groupValues[2].toInt()
                val units = (n1 + n2 - 1) / n2
                return (1..units).map { "${it * n2 - n2 + 1}~${it * n2}" }
            }
            m = Regex("CUSTOM,(.+)").find(units)
            if (m != null)
                return m.groupValues[1].split(",")
            return listOf()
        }
        retrofitJson.create(RestTextbook::class.java)
            .getDataByLang("LANGID,eq,$langid")
            .lst!!.also {
                for (o in it) {
                    o.lstUnits = f(o.units).mapIndexed { index, s -> MSelectItem(index + 1, s) }
                    o.lstParts = o.parts.split(",").mapIndexed { index, s -> MSelectItem(index + 1, s) }
                }
            }
    }

}
