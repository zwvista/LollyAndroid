package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.MutableLiveData
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.services.wpp.PatternWebPageService
import com.zwstudio.lolly.services.wpp.WebPageService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.views.applyIO
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PatternsWebPagesViewModel : DrawerListViewModel(), KoinComponent {

    var lstWebPages_ = MutableLiveData(mutableListOf<MPatternWebPage>())
    var lstWebPages get() = lstWebPages_.value!!; set(v) { lstWebPages_.value = v }
    fun getWebPageText(position: Int) = "${position + 1}/${lstWebPages.size} ${lstWebPages[position].title}"

    lateinit var compositeDisposable: CompositeDisposable

    private val patternWebPageService by inject<PatternWebPageService>()
    private val webPageService by inject<WebPageService>()

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
