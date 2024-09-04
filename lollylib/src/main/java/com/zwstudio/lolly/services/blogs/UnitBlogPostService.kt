package com.zwstudio.lolly.services.blogs

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MUnitBlogPost
import com.zwstudio.lolly.restapi.blogs.RestUnitBlogPost
import io.reactivex.rxjava3.core.Single

class UnitBlogPostService {
    fun getDataByTextbook(textbookid: Int, unit: Int): Single<List<MUnitBlogPost>> =
        retrofitJson.create(RestUnitBlogPost::class.java)
            .getDataByTextbook("TEXTBOOKID,eq,${textbookid}", "UNIT,eq,${unit}")
            .map { it.lst!! }

    fun update(o: MUnitBlogPost) =
        retrofitJson.create(RestUnitBlogPost::class.java)
            .update(o.id, o.textbookid, o.unit, o.content)
            .let { Log.d("", it.toString()) }

    fun create(o: MUnitBlogPost) =
        retrofitJson.create(RestUnitBlogPost::class.java)
            .create(o.textbookid, o.unit, o.content)
            .let { Log.d("", it.toString()) }
}
