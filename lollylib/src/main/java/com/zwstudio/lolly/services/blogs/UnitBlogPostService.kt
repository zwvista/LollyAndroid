package com.zwstudio.lolly.services.blogs

import com.zwstudio.lolly.common.logUpdate
import com.zwstudio.lolly.common.logCreate
import com.zwstudio.lolly.common.retrofitJson
import com.zwstudio.lolly.models.blogs.MUnitBlogPost
import com.zwstudio.lolly.restapi.blogs.RestUnitBlogPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UnitBlogPostService {
    private val api = retrofitJson.create(RestUnitBlogPost::class.java)

    suspend fun getDataByTextbook(textbookid: Int, unit: Int): MUnitBlogPost? = withContext(Dispatchers.IO) {
        api.getDataByTextbook("TEXTBOOKID,eq,$textbookid", "UNIT,eq,$unit")
            .lst.firstOrNull()
    }

    private suspend fun create(item: MUnitBlogPost) = withContext(Dispatchers.IO) {
        api.create(item).logCreate()
    }

    private suspend fun update(item: MUnitBlogPost) = withContext(Dispatchers.IO) {
        api.update(item.id, item).logUpdate(item.id)
    }

    suspend fun update(textbookid: Int, unit: Int, content: String) {
        val item = getDataByTextbook(textbookid, unit) ?: MUnitBlogPost().apply {
            this.textbookid = textbookid
            this.unit = unit
        }
        item.content = content
        if (item.id == 0) create(item) else update(item)
    }
}
