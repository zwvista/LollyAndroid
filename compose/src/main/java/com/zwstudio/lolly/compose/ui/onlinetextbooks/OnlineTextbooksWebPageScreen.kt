package com.zwstudio.lolly.compose.ui.onlinetextbooks

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
import com.zwstudio.lolly.viewmodels.onlinetextbooks.OnlineTextbooksWebPageViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun OnlineTextbooksWebPageScreen(vm: OnlineTextbooksWebPageViewModel, navController: NavHostController?) {

    var wv: WebView? = remember { null }
    LaunchedEffect(Unit, block = {
        vm.selectedOnlineTextbookIndex_.onEach {
            wv?.loadUrl(vm.selectedOnlineTextbook.url)
        }.launchIn(this)
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.onlineTextbooks_webpage),
            navController = navController
        )
        Spinner(
            modifier = Modifier
                .background(color = colorResource(R.color.color_text3))
                .fillMaxWidth(),
            items = vm.lstOnlineTextbooks,
            selectedItemIndexStateFlow = vm.selectedOnlineTextbookIndex_,
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
