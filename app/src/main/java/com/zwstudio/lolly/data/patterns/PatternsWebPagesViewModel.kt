package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.android.applyIO
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.service.wpp.PatternWebPageService
import com.zwstudio.lolly.service.wpp.WebPageService
import io.reactivex.rxjava3.disposables.CompositeDisposable

class PatternsWebPagesViewModel : ViewModel() {

    var lstWebPages_ = MutableLiveData(mutableListOf<MPatternWebPage>())
    var lstWebPages get() = lstWebPages_.value!!; set(v) { lstWebPages_.value = v }
    var isSwipeStarted_ = MutableLiveData(false)
    var isSwipeStarted get() = isSwipeStarted_.value!!; set(v) { isSwipeStarted_.value = v }
    var isEditMode_ = MutableLiveData(false)
    var isEditMode get() = isEditMode_.value!!; set(v) { isEditMode_.value = v }

    lateinit var compositeDisposable: CompositeDisposable

    val patternWebPageService = PatternWebPageService()
    val webPageService = WebPageService()

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
