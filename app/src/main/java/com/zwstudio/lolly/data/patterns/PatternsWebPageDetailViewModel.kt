package com.zwstudio.lolly.data.patterns

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.domain.wpp.MPatternWebPage

class PatternsWebPageDetailViewModel(val item: MPatternWebPage) : ViewModel() {
    val id = MutableLiveData(item.id)
    val patternid = MutableLiveData(item.patternid)
    val pattern = MutableLiveData(item.pattern)
    val webpageid = MutableLiveData(item.webpageid)
    val title = MutableLiveData(item.title)
    val url = MutableLiveData(item.url)

    fun save() {
        item.id = id.value!!
        item.patternid = patternid.value!!
        item.pattern = pattern.value!!
        item.webpageid = webpageid.value!!
        item.title = title.value!!
        item.url = url.value!!
    }
}
