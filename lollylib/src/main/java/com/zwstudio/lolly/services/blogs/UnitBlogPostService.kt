package com.zwstudio.lolly.services.blogs

import android.util.Log
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MUnitBlogPost
import com.zwstudio.lolly.restapi.blogs.RestUnitBlogPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitBlogPostService {
    suspend fun getDataByTextbook(textbookid: Int, unit: Int): MUnitBlogPost? = withContext(Dispatchers.IO) {
        val lst = retrofitJson.create(RestUnitBlogPost::class.java)
            .getDataByTextbook("TEXTBOOKID,eq,${textbookid}", "UNIT,eq,${unit}")
            .lst!!
        return@withContext if (lst.isEmpty()) null else lst[0]
    }

    private suspend fun update(o: MUnitBlogPost) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitBlogPost::class.java)
            .update(o.id, o.textbookid, o.unit, o.content)
            .let { Log.d("", it.toString()) }
    }

    private suspend fun create(o: MUnitBlogPost): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestUnitBlogPost::class.java)
            .create(o.textbookid, o.unit, o.content)
            .also { Log.d("", it.toString()) }
    }

    suspend fun update(textbookid: Int, unit: Int, content: String) {
        val o = getDataByTextbook(textbookid, unit)
        val item = o ?: MUnitBlogPost()
        if (item.id == 0) {
            item.textbookid = textbookid
            item.unit = unit
        }
        item.content = content
        if (item.id == 0) create(item) else update(item)
    }
}
