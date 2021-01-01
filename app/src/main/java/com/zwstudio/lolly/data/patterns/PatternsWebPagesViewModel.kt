package com.zwstudio.lolly.data.patterns

import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.service.wpp.PatternWebPageService
import com.zwstudio.lolly.service.wpp.WebPageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsWebPagesViewModel : BaseViewModel() {

    var lstWebPages = mutableListOf<MPatternWebPage>()
    var isSwipeStarted = false
    var isEditMode = false

    lateinit var compositeDisposable: CompositeDisposable

    @Bean
    lateinit var patternWebPageService: PatternWebPageService
    @Bean
    lateinit var webPageService: WebPageService

    fun getWebPages(patternid: Int) =
        patternWebPageService.getDataByPattern(patternid)
            .applyIO()
            .map { lstWebPages.clear(); lstWebPages.addAll(it) }

    fun updatePatternWebPage(item: MPatternWebPage) =
        patternWebPageService.update(item)
    fun createPatternWebPage(item: MPatternWebPage) =
        patternWebPageService.create(item)
            .applyIO()
            .map {
                item.id = it
                lstWebPages.add(item)
            }
    fun deletePatternWebPage(id: Int) =
        patternWebPageService.delete(id)

    fun updateWebPage(item: MPatternWebPage) =
        webPageService.update(item)
    fun createWebPage(item: MPatternWebPage) =
        webPageService.create(item)
            .applyIO()
            .map { item.id = it }
    fun deleteWebPage(id: Int) =
        webPageService.delete(id)

    fun newPatternWebPage(patternid: Int, pattern: String) = MPatternWebPage().apply {
        this.patternid = patternid
        this.pattern = pattern
        seqnum = (lstWebPages.maxOfOrNull { it.seqnum } ?: 0) + 1
    }

    fun reindexWebPage(onNext: (Int) -> Unit) {
        for (i in 1..lstWebPages.size) {
            val item = lstWebPages[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            compositeDisposable.add(patternWebPageService.updateSeqNum(item.id, i).subscribe {
                onNext(i - 1)
            })
        }
    }
}
