package com.zwstudio.lolly.viewmodels.blogs

import androidx.lifecycle.viewModelScope
import com.zwstudio.lolly.models.blogs.MLangBlogGroup
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.services.blogs.LangBlogGroupService
import com.zwstudio.lolly.services.blogs.LangBlogPostContentService
import com.zwstudio.lolly.services.blogs.LangBlogPostService
import com.zwstudio.lolly.viewmodels.DrawerListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class LangBlogViewModel : DrawerListViewModel(), KoinComponent {

    var lstLangBlogGroupsAll_ = MutableStateFlow(listOf<MLangBlogGroup>())
    var lstLangBlogGroupsAll get() = lstLangBlogGroupsAll_.value; set(v) { lstLangBlogGroupsAll_.value = v }
    var lstLangBlogGroups_ = MutableStateFlow(listOf<MLangBlogGroup>())
    var lstLangBlogGroups get() = lstLangBlogGroups_.value; set(v) { lstLangBlogGroups_.value = v }
    var groupFilter_ = MutableStateFlow("")
    var groupFilter get() = groupFilter_.value; set(v) { groupFilter_.value = v }
    private val noGroupFilter get() = groupFilter.isEmpty()
    var selectedGroup_ = MutableStateFlow<MLangBlogGroup?>(null)
    var selectedGroup get() = selectedGroup_.value; set(v) { selectedGroup_.value = v }

    var lstLangBlogPostsAll_ = MutableStateFlow(listOf<MLangBlogPost>())
    var lstLangBlogPostsAll get() = lstLangBlogPostsAll_.value; set(v) { lstLangBlogPostsAll_.value = v }
    var lstLangBlogPosts_ = MutableStateFlow(listOf<MLangBlogPost>())
    var lstLangBlogPosts get() = lstLangBlogPosts_.value; set(v) { lstLangBlogPosts_.value = v }
    var postFilter_ = MutableStateFlow("")
    var postFilter get() = postFilter_.value; set(v) { postFilter_.value = v }
    private val noPostFilter get() = postFilter.isEmpty()
    var selectedPost_ = MutableStateFlow<MLangBlogPost?>(null)
    var selectedPost get() = selectedPost_.value; set(v) { selectedPost_.value = v }

    var postContent_ = MutableStateFlow("")
    var postContent get() = postContent_.value; set(v) { postContent_.value = v }

    protected val langBlogGroupService by inject<LangBlogGroupService>()
    protected val langBlogPostService by inject<LangBlogPostService>()
    protected val langBlogPostContentService by inject<LangBlogPostContentService>()

    init {
        combine(lstLangBlogGroupsAll_, groupFilter_, ::Pair).onEach {
            lstLangBlogGroups = if (noGroupFilter) lstLangBlogGroupsAll else lstLangBlogGroupsAll.filter {
                it.groupname.contains(groupFilter, true)
            }
        }.launchIn(viewModelScope)
        combine(lstLangBlogPostsAll_, postFilter_, ::Pair).onEach {
            lstLangBlogPosts = if (noPostFilter) lstLangBlogPostsAll else lstLangBlogPostsAll.filter {
                it.title.contains(postFilter, true)
            }
        }.launchIn(viewModelScope)
        selectedPost_.onEach {
            postContent = langBlogPostContentService.getDataById(it?.id ?: 0)?.content ?: ""
        }.launchIn(viewModelScope)
    }
}
