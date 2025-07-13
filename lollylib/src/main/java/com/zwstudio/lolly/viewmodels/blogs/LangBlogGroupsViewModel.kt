package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.blogs.MLangBlogGroup
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import kotlinx.coroutines.launch

class LangBlogGroupsViewModel: LangBlogViewModel() {
    init {
        viewModelScope.launch { getGroups() }
    }

    suspend fun getGroups() {
        isBusy = true
        lstLangBlogGroupsAll = langBlogGroupService.getDataByLang(vmSettings.selectedLang.id)
        isBusy = false
    }

    suspend fun getPosts() {
        isBusy = true
        lstLangBlogPostsAll = langBlogPostService.getDataByLangGroup(
            vmSettings.selectedLang.id, selectedGroup?.id ?: 0)
        isBusy = false
    }

    suspend fun selectGroup(group: MLangBlogGroup?) {
        selectedGroup = group
        getPosts()
    }

    suspend fun selectPost(post: MLangBlogPost?) {
        selectedPost = post
        postContent = langBlogPostContentService.getDataById(post?.id ?: 0)?.content ?: ""
    }
}