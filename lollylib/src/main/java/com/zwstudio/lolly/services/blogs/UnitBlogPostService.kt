package com.zwstudio.lolly.services.blogs

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MUnitBlogPost
import com.zwstudio.lolly.restapi.blogs.RestUnitBlogPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitBlogPostService {
    suspend fun getDataByTextbook(textbookid: Int, unit: Int): List<MUnitBlogPost> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitBlogPost::class.java)
            .getDataByTextbook("TEXTBOOKID,eq,${textbookid}", "UNIT,eq,${unit}")
            .lst!!
    }

    suspend fun update(o: MUnitBlogPost) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitBlogPost::class.java)
            .update(o.id, o.textbookid, o.unit, o.content)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MUnitBlogPost): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitBlogPost::class.java)
            .create(o.textbookid, o.unit, o.content)
            .also { Log.d("", it.toString()) }
    }
}
