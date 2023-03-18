package com.zwstudio.lolly.ui.patterns

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.zwstudio.lolly.R
import com.zwstudio.lolly.models.wpp.MPattern
import com.zwstudio.lolly.ui.common.TopBarArrow

@Composable
fun PatternsWebPageScreen(item: MPattern, navController: NavHostController?) {

    var wv: WebView? = remember { null }
    LaunchedEffect(Unit, block = {
        wv?.loadUrl(item.url)
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarArrow(
            title = stringResource(id = R.string.patterns_webpage),
            navController = navController
        )
        Text(
            text = item.title,
            color = Color.White,
            modifier = Modifier
                .background(color = colorResource(R.color.color_text3))
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentSize(Alignment.Center),
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
