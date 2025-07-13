package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.models.blogs.MLangBlogGroup
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.launch

class LangBlogGroupsViewModel: LangBlogViewModel() {
    init {
        viewModelScope.launch { getGroups() }
    }

    fun getGroups(): Completable {
        isBusy = true
        return langBlogGroupService.getDataByLang(vmSettings.selectedLang.id)
            .applyIO()
            .flatMapCompletable { lstLangBlogGroupsAll = it ; isBusy = false; Completable.complete() }
    }

    fun getPosts(): Completable {
        isBusy = true
        return langBlogPostService.getDataByLangGroup(
            vmSettings.selectedLang.id, selectedGroup?.id ?: 0)
            .applyIO()
            .flatMapCompletable { lstLangBlogPostsAll = it ; isBusy = false; Completable.complete() }
    }

    fun selectGroup(group: MLangBlogGroup?): Completable {
        selectedGroup = group
        return getPosts()
    }

    fun selectPost(post: MLangBlogPost?): Completable {
        selectedPost = post
        return langBlogPostContentService.getDataById(post?.id ?: 0)
            .applyIO()
            .flatMapCompletable { postContent = it.map { it.content }.orElse(""); Completable.complete() }
    }
}