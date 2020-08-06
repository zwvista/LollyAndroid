package com.zwstudio.lolly.service

import com.zwstudio.lolly.domain.MSelectItem
import com.zwstudio.lolly.domain.MTextbook
import com.zwstudio.lolly.restapi.RestTextbook
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class TextbookService: BaseService() {
    fun getDataByLang(langid: Int): Observable<List<MTextbook>> {
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
        return retrofitJson.create(RestTextbook::class.java)
            .getDataByLang("LANGID,eq,${langid}")
            .map {
                val lst = it.lst!!
                for (o in lst) {
                    o.lstUnits = f(o.units).mapIndexed { index, s -> MSelectItem(index + 1, s) }
                    o.lstParts = o.parts.split(",").mapIndexed { index, s -> MSelectItem(index + 1, s) }
                }
                lst
            }
    }

}
