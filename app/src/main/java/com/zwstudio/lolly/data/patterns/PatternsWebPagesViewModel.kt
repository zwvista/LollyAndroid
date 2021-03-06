package com.zwstudio.lolly.data.patterns

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.service.wpp.PatternWebPageService
import com.zwstudio.lolly.service.wpp.WebPageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsWebPagesViewModel : BaseViewModel() {

    var lstWebPages = mutableListOf<MPatternWebPage>()
    var isSwipeStarted = false
    var isEditMode = false

    @Bean
    lateinit var patternWebPageService: PatternWebPageService
    @Bean
    lateinit var webPageService: WebPageService

    suspend fun getWebPages(patternid: Int) {
        val lst = patternWebPageService.getDataByPattern(patternid)
        withContext(Dispatchers.Main) { lstWebPages.clear(); lstWebPages.addAll(lst) }
    }

    suspend fun updatePatternWebPage(item: MPatternWebPage) =
        patternWebPageService.update(item)
    suspend fun createPatternWebPage(item: MPatternWebPage) {
        item.id = patternWebPageService.create(item)
        lstWebPages.add(item)
    }
    suspend fun deletePatternWebPage(id: Int) =
        patternWebPageService.delete(id)

    suspend fun updateWebPage(item: MPatternWebPage) =
        webPageService.update(item)
    suspend fun createWebPage(item: MPatternWebPage) {
        item.id = webPageService.create(item)
    }
    suspend fun deleteWebPage(id: Int) =
        webPageService.delete(id)

    fun newPatternWebPage(patternid: Int, pattern: String) = MPatternWebPage().apply {
        this.patternid = patternid
        this.pattern = pattern
        seqnum = (lstWebPages.maxOfOrNull { it.seqnum } ?: 0) + 1
    }

    suspend fun reindexWebPage(onNext: (Int) -> Unit) {
        for (i in 1..lstWebPages.size) {
            val item = lstWebPages[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            patternWebPageService.updateSeqNum(item.id, i)
            onNext(i - 1)
        }
    }
}
