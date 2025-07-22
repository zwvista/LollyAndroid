package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent

class LangBlogPostsContentViewModel(val vmGroups: LangBlogGroupsViewModel, val lstPosts: List<MLangBlogPost>, index: Int): ViewModel(), KoinComponent {
    var selectedPostIndex_ = MutableStateFlow(index)
    var selectedPostIndex get() = selectedPostIndex_.value; set(v) { selectedPostIndex_.value = v }
    val selectedPost: MLangBlogPost
        get() = lstPosts[selectedPostIndex]

    fun next(delta: Int) {
        selectedPostIndex = (selectedPostIndex + delta + lstPosts.size) % lstPosts.size
    }

    init {
        selectedPostIndex_.onEach {
            vmGroups.selectedPost = selectedPost
        }.launchIn(viewModelScope)
    }
}
