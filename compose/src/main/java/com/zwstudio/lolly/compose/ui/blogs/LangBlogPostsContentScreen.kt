package com.zwstudio.lolly.compose.ui.blogs

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.zwstudio.lolly.common.OnSwipeWebviewTouchListener
import com.zwstudio.lolly.common.TouchListener
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarArrow
import com.zwstudio.lolly.models.blogs.MLangBlogPost
import com.zwstudio.lolly.services.misc.BlogService
import com.zwstudio.lolly.viewmodels.blogs.LangBlogGroupsViewModel
import com.zwstudio.lolly.viewmodels.blogs.LangBlogPostsContentViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LangBlogPostsContentScreen(
    lstLangBlogPosts: List<MLangBlogPost>,
    index: Int,
    vmGroup: LangBlogGroupsViewModel,
    navController: NavHostController?
) {
    val vm = koinViewModel<LangBlogPostsContentViewModel>(
        parameters = { parametersOf(lstLangBlogPosts, index) }
    )
    var wv: WebView? = remember { null }
    LaunchedEffect(Unit) {
        vm.selectedPostIndex_.onEach {
            vmGroup.selectedPost = vm.selectedPost
        }.launchIn(this)

        vmGroup.postHtml_.onEach {
            wv?.loadData(it, null, null)
        }.launchIn(this)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.lang_blog_posts_content),
            navController = navController
        )
        Spinner(
            modifier = Modifier
                .background(color = colorResource(R.color.color_text3))
                .fillMaxWidth(),
            items = vm.lstPosts,
            selectedItemIndexStateFlow = vm.selectedPostIndex_,
            itemText = { it.title }
        )
        AndroidView(
            factory = {
                WebView(it).apply {
                    wv = this
                    webViewClient = WebViewClient()
                    setOnTouchListener(OnSwipeWebviewTouchListener(context, object : TouchListener {
                        override fun onSwipeLeft() =
                            vm.next(-1)
                        override fun onSwipeRight() =
                            vm.next(1)
                    }))
                }
            },
            modifier = Modifier.weight(1f)
        )
    }
}
