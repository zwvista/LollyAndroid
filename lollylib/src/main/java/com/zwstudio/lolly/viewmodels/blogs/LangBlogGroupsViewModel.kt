package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.common.applyIO
import com.zwstudio.lolly.common.vmSettings
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LangBlogGroupsViewModel: LangBlogViewModel() {
    init {
        viewModelScope.launch {
            compositeDisposable.add(getGroups().subscribe())
        }
        selectedGroup_.onEach {
            compositeDisposable.add(getPosts().subscribe())
        }.launchIn(viewModelScope)
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
}