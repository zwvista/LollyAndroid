package com.zwstudio.lolly.ui.patterns

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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarArrow
import com.zwstudio.lolly.viewmodels.patterns.PatternsWebPagesViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun PatternsWebPagesBrowseScreen(vm: PatternsWebPagesViewModel, item: MPattern, navController: NavHostController?) {

    var wv: WebView? = remember { null }
    LaunchedEffect(Unit, block = {
        vm.getWebPages(item.id)
        vm.currentWebPageIndex_.filter { it != -1 }.onEach {
            wv?.loadUrl(vm.lstWebPages[it].url)
        }.launchIn(this)
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = "",
            navController = navController
        )
        Spinner(
            modifier = Modifier
                .background(color = colorResource(R.color.color_text3))
                .fillMaxWidth(),
            itemsStateFlow = vm.lstWebPages_,
            selectedItemIndexStateFlow = vm.currentWebPageIndex_,
            itemText = { it.title }
        )
        AndroidView(
            factory = {
                WebView(it).apply {
                    wv = this
                    webViewClient = WebViewClient()
                }
            },
            modifier = Modifier.weight(1f)
        )
    }
}
