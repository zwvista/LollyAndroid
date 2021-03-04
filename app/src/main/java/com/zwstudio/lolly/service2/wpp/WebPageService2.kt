package com.zwstudio.lolly.service2.wpp

import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.domain.wpp.MWebPage
import com.zwstudio.lolly.rest2api.wpp.Rest2WebPage
import com.zwstudio.lolly.restapi.wpp.RestWebPage
import com.zwstudio.lolly.service.misc.BaseService
import com.zwstudio.lolly.service2.misc.BaseService2
import io.reactivex.rxjava3.core.Observable
import org.androidannotations.annotations.EBean

@EBean
class WebPageService2: BaseService2() {
    suspend fun getDataBySearch(title: String, url: String): List<MWebPage> =
        retrofitJson2.create(Rest2WebPage::class.java)
            .getDataBySearch("TITLE,cs,$title", "URL,cs,$url")
            .lst!!

    suspend fun getDataById(id: Int): List<MWebPage> =
        retrofitJson2.create(Rest2WebPage::class.java)
            .getDataById("ID,eq,$id")
            .lst!!

    suspend fun update(o: MPatternWebPage) =
        retrofitJson2.create(Rest2WebPage::class.java)
            .update(o.webpageid, o.title, o.url)
            .let { println(it.toString()) }

    suspend fun create(o: MPatternWebPage): Int =
        retrofitJson2.create(Rest2WebPage::class.java)
            .create(o.title, o.url)
            .also { println(it.toString()) }

    suspend fun update(o: MWebPage) =
        retrofitJson2.create(Rest2WebPage::class.java)
            .update(o.id, o.title, o.url)
            .let { println(it.toString()) }

    suspend fun create(o: MWebPage): Int =
        retrofitJson2.create(Rest2WebPage::class.java)
            .create(o.title, o.url)
            .also { println(it.toString()) }

    suspend fun delete(id: Int) =
        retrofitJson2.create(Rest2WebPage::class.java)
            .delete(id)
            .let { println(it.toString()) }
}
