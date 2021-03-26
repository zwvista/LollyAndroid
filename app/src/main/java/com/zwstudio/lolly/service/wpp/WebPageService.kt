package com.zwstudio.lolly.service.wpp

import android.util.Log
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.domain.wpp.MWebPage
import com.zwstudio.lolly.restapi.wpp.RestWebPage
import com.zwstudio.lolly.service.misc.BaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.EBean

@EBean
class WebPageService: BaseService() {
    suspend fun getDataBySearch(title: String, url: String): List<MWebPage> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .getDataBySearch("TITLE,cs,$title", "URL,cs,$url")
            .lst!!
    }

    suspend fun getDataById(id: Int): List<MWebPage> = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .getDataById("ID,eq,$id")
            .lst!!
    }

    suspend fun update(o: MPatternWebPage) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .update(o.webpageid, o.title, o.url)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MPatternWebPage): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .create(o.title, o.url)
            .also { Log.d("", it.toString()) }
    }

    suspend fun update(o: MWebPage) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .update(o.id, o.title, o.url)
            .let { Log.d("", it.toString()) }
    }

    suspend fun create(o: MWebPage): Int = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .create(o.title, o.url)
            .also { Log.d("", it.toString()) }
    }

    suspend fun delete(id: Int) = withContext(Dispatchers.IO) {
        retrofitJson.create(RestWebPage::class.java)
            .delete(id)
            .let { Log.d("", it.toString()) }
    }
}
