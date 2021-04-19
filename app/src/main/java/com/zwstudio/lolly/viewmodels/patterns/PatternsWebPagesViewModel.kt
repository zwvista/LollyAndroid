package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import com.zwstudio.lolly.services.wpp.PatternWebPageService
import com.zwstudio.lolly.services.wpp.WebPageService
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PatternsWebPagesViewModel : DrawerListViewModel(), KoinComponent {

    var lstWebPages_ = MutableLiveData(mutableListOf<MPatternWebPage>())
    var lstWebPages get() = lstWebPages_.value!!; set(v) { lstWebPages_.value = v }

    private val patternWebPageService by inject<PatternWebPageService>()
    private val webPageService by inject<WebPageService>()

    fun getWebPages(patternid: Int) = viewModelScope.launch {
        val lst = patternWebPageService.getDataByPattern(patternid)
        lstWebPages.clear(); lstWebPages.addAll(lst)
    }

    fun updatePatternWebPage(item: MPatternWebPage) = viewModelScope.launch {
        patternWebPageService.update(item)
    }
    fun createPatternWebPage(item: MPatternWebPage) = viewModelScope.launch {
        item.id = patternWebPageService.create(item)
        lstWebPages.add(item)
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
        seqnum = (lstWebPages.maxOfOrNull { it.seqnum } ?: 0) + 1
    }

    fun reindexWebPage(onNext: (Int) -> Unit) = viewModelScope.launch {
        for (i in 1..lstWebPages.size) {
            val item = lstWebPages[i - 1]
            if (item.seqnum == i) continue
            item.seqnum = i
            patternWebPageService.updateSeqNum(item.id, i)
            onNext(i - 1)
        }
    }
}
