package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.vmSettings
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LangBlogGroupsViewModel: LangBlogViewModel() {
    init {
        viewModelScope.launch { getGroups() }
        selectedGroup_.onEach {
            getPosts()
        }.launchIn(viewModelScope)
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
}