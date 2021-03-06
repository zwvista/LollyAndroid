package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.data.misc.BaseViewModel
import com.zwstudio.lolly.data.misc.applyIO
import com.zwstudio.lolly.domain.wpp.MPatternWebPage
import com.zwstudio.lolly.service.wpp.PatternWebPageService
import com.zwstudio.lolly.service.wpp.WebPageService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.androidannotations.annotations.Bean
import org.androidannotations.annotations.EBean

@EBean
class PatternsWebPagesViewModel : BaseViewModel() {

    var lstWebPages = MutableLiveData(mutableListOf<MPatternWebPage>())
    var isSwipeStarted = MutableLiveData(false)
    var isEditMode = MutableLiveData(false)

    @Bean
    lateinit var patternWebPageService: PatternWebPageService
    @Bean
    lateinit var webPageService: WebPageService

    fun getWebPages(patternid: Int) = viewModelScope.launch {
        val lst = patternWebPageService.getDataByPattern(patternid)
        lstWebPages.value!!.clear(); lstWebPages.value!!.addAll(lst)
    }

    fun updatePatternWebPage(item: MPatternWebPage) = viewModelScope.launch {
        patternWebPageService.update(item)
    }
    fun createPatternWebPage(item: MPatternWebPage) = viewModelScope.launch {
        item.id = patternWebPageService.create(item)
        lstWebPages.value!!.add(item)
    }
    fun deletePatternWebPage(id: Int) = viewModelScope.launch {
        patternWebPageService.delete(id)
    }

    fun updateWebPage(item: MPatternWebPage) = viewModelScope.launch {
        webPageService.update(item)
    }
    fun createWebPage(item: MPatternWebPage) = viewModelScope.launch {
        item.id = webPageService.create(item)
    }
    fun deleteWebPage(id: Int) = viewModelScope.launch {
        webPageService.delete(id)
    }

    fun newPatternWebPage(patternid: Int, pattern: String) = MPatternWebPage().apply {
        this.patternid = patternid
        this.pattern = pattern
        seqnum = (lstWebPages.value!!.maxOfOrNull { it.seqnum } ?: 0) + 1
    }

    fun reindexWebPage(onNext: (Int) -> Unit) = viewModelScope.launch {
        for (i in 1..lstWebPages.value!!.size) {
            val item = lstWebPages.value!![i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            patternWebPageService.updateSeqNum(item.id, i)
            onNext(i - 1)
        }
    }
}
