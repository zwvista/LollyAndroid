package com.zwstudio.lolly.services.wpp

import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.models.wpp.MWebPage
import com.zwstudio.lolly.restapi.wpp.RestWebPage
import com.zwstudio.lolly.services.misc.retrofitJson
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class WebPageService {
    fun getDataBySearch(title: String, url: String): Single<List<MWebPage>> =
        retrofitJson.create(RestWebPage::class.java)
            .getDataBySearch("TITLE,cs,$title", "URL,cs,$url")
            .map { it.lst!! }

    fun getDataById(id: Int): Single<List<MWebPage>> =
        retrofitJson.create(RestWebPage::class.java)
            .getDataById("ID,eq,$id")
            .map { it.lst!! }

    fun update(o: MPatternWebPage): Completable =
        retrofitJson.create(RestWebPage::class.java)
            .update(o.webpageid, o.title, o.url)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun create(o: MPatternWebPage): Single<Int> =
        retrofitJson.create(RestWebPage::class.java)
            .create(o.title, o.url)
            .doAfterSuccess { println(it.toString()) }

    fun update(o: MWebPage): Completable =
        retrofitJson.create(RestWebPage::class.java)
            .update(o.id, o.title, o.url)
            .flatMapCompletable { println(it.toString()); Completable.complete() }

    fun create(o: MWebPage): Single<Int> =
        retrofitJson.create(RestWebPage::class.java)
            .create(o.title, o.url)
            .doAfterSuccess { println(it.toString()) }

    fun delete(id: Int): Completable =
        retrofitJson.create(RestWebPage::class.java)
            .delete(id)
            .flatMapCompletable { println(it.toString()); Completable.complete() }
}
