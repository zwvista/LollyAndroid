package com.zwstudio.lolly.services.blogs

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MUnitBlogPost
import com.zwstudio.lolly.restapi.blogs.RestUnitBlogPost
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import java.util.Optional

class UnitBlogPostService {
    fun getDataByTextbook(textbookid: Int, unit: Int): Single<Optional<MUnitBlogPost>> =
        retrofitJson.create(RestUnitBlogPost::class.java)
            .getDataByTextbook("TEXTBOOKID,eq,${textbookid}", "UNIT,eq,${unit}")
            .map { Optional.ofNullable(it.lst!!.firstOrNull()) }

    private fun update(o: MUnitBlogPost): Completable =
        retrofitJson.create(RestUnitBlogPost::class.java)
            .update(o.id, o.textbookid, o.unit, o.content)
            .flatMapCompletable { Log.d("", it.toString()); Completable.complete() }

    private fun create(o: MUnitBlogPost): Completable =
        retrofitJson.create(RestUnitBlogPost::class.java)
            .create(o.textbookid, o.unit, o.content)
            .flatMapCompletable { Log.d("", it.toString()); Completable.complete() }

    fun update(textbookid: Int, unit: Int, content: String): Completable =
        getDataByTextbook(textbookid, unit)
            .map { it.orElse(MUnitBlogPost()) }
            .flatMapCompletable {
                if (it.id == 0) {
                    it.textbookid = textbookid
                    it.unit = unit
                }
                it.content = content
                if (it.id == 0) create(it) else update(it)
            }
}
