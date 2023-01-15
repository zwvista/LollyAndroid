package com.zwstudio.lolly.viewmodels.patterns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.wpp.MPatternWebPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PatternsWebPagesDetailViewModel(val item: MPatternWebPage) : ViewModel() {
    val id = item.id
    val patternid = item.patternid
    val pattern = item.pattern
    val seqnum = MutableStateFlow(item.seqnum.toString())
    val webpageid = item.webpageid
    val title = MutableStateFlow(item.title)
    val url = MutableStateFlow(item.url)
    val saveEnabled = MutableStateFlow(false)

    init {
        combine(title, url, ::Pair).onEach { (t, u) ->
            saveEnabled.value = t.isNotEmpty() && u.isNotEmpty()
        }.launchIn(viewModelScope)
    }

    fun save() {
        item.seqnum = seqnum.value.toInt()
        item.title = title.value
        item.url = url.value
    }
}
