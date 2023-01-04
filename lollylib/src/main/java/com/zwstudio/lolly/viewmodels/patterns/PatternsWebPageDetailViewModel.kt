package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import kotlinx.coroutines.flow.MutableStateFlow

class PatternsWebPageDetailViewModel(val item: MPatternWebPage) : ViewModel() {
    val id = MutableStateFlow(item.id)
    val patternid = MutableStateFlow(item.patternid)
    val pattern = MutableStateFlow(item.pattern)
    val webpageid = MutableStateFlow(item.webpageid)
    val title = MutableStateFlow(item.title)
    val url = MutableStateFlow(item.url)

    fun save() {
        item.id = id.value
        item.patternid = patternid.value
        item.pattern = pattern.value
        item.webpageid = webpageid.value
        item.title = title.value
        item.url = url.value
    }
}
