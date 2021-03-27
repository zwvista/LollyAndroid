package com.zwstudio.lolly.service.wpp

import com.zwstudio.lolly.android.retrofitJson
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.domain.wpp.MWebPage
import com.zwstudio.lolly.restapi.wpp.RestWebPage
import io.reactivex.rxjava3.core.Observable

class WebPageService {
    fun getDataBySearch(title: String, url: String): Observable<List<MWebPage>> =
        retrofitJson.create(RestWebPage::class.java)
            .getDataBySearch("TITLE,cs,$title", "URL,cs,$url")
            .map { it.lst!! }

    fun getDataById(id: Int): Observable<List<MWebPage>> =
        retrofitJson.create(RestWebPage::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun update(o: MPatternWebPage): Observable<Unit> =
        retrofitJson.create(RestWebPage::class.java)
            .update(o.webpageid, o.title, o.url)
            .map { println(it.toString()) }

    fun create(o: MPatternWebPage): Observable<Int> =
        retrofitJson.create(RestWebPage::class.java)
            .create(o.title, o.url)
            .doOnNext { println(it.toString()) }

    fun update(o: MWebPage): Observable<Unit> =
        retrofitJson.create(RestWebPage::class.java)
            .update(o.id, o.title, o.url)
            .map { println(it.toString()) }

    fun create(o: MWebPage): Observable<Int> =
        retrofitJson.create(RestWebPage::class.java)
            .create(o.title, o.url)
            .doOnNext { println(it.toString()) }

    fun delete(id: Int): Observable<Unit> =
        retrofitJson.create(RestWebPage::class.java)
            .delete(id)
            .map { println(it.toString()) }
}
