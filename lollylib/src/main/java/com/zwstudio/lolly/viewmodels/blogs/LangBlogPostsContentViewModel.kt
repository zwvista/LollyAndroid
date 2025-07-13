package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.ViewModel
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent

class LangBlogPostsContentViewModel(val lstLangBlogPosts: List<MLangBlogPost>, index: Int): ViewModel(), KoinComponent {
    var selectedLangBlogPostIndex_ = MutableStateFlow(index)
    var selectedLangBlogPostIndex get() = selectedLangBlogPostIndex_.value; set(v) { selectedLangBlogPostIndex_.value = v }
    val selectedLangBlogPost: MLangBlogPost
        get() = lstLangBlogPosts[selectedLangBlogPostIndex]

    fun next(delta: Int) {
        selectedLangBlogPostIndex = (selectedLangBlogPostIndex + delta + lstLangBlogPosts.size) % lstLangBlogPosts.size
    }
}
