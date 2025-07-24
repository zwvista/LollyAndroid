package com.zwstudio.lolly.compose.ui.misc

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.zwstudio.lolly.common.OnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.compose.R
import com.zwstudio.lolly.compose.ui.common.DrawerScreens
import com.zwstudio.lolly.compose.ui.common.SearchView
import com.zwstudio.lolly.compose.ui.common.Spinner
import com.zwstudio.lolly.compose.ui.common.TopBarMenu
import com.zwstudio.lolly.compose.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(openDrawer: () -> Unit) {

    val vm = koinViewModel<SearchViewModel>()
    val onlineDict = remember { OnlineDict() }
    val context = LocalContext.current
    // https://stackoverflow.com/questions/64181930/request-focus-on-textfield-in-jetpack-compose
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
        if (GlobalUserViewModel.isLoggedIn) {
            vmSettings.getData()
            vmSettings.selectedDictReferenceIndex_.onEach {
                onlineDict.searchDict()
            }.launchIn(this)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = stringResource(id = DrawerScreens.Search.titleRes),
            onButtonClicked = { openDrawer() },
            actions = {
                IconButton(onClick = {
                    GlobalUserViewModel.remove(context)
                }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                }
            }
        )
        SearchView(
            valueStateFlow = vm.word_,
            modifier = Modifier.focusRequester(focusRequester)
        ) {
            coroutineScope.launch {
                onlineDict.searchDict()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .weight(1f),
                itemsStateFlow = vmSettings.lstLanguages_,
                selectedItemIndexStateFlow = vmSettings.selectedLangIndex_,
                itemText = { it.langname }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .weight(1f),
                itemsStateFlow = vmSettings.lstDictsReference_,
                selectedItemIndexStateFlow = vmSettings.selectedDictReferenceIndex_,
                itemText = { it.dictname }
            )
        }
        AndroidView(
            factory = {
                WebView(it).apply {
                    onlineDict.wv = this
                    onlineDict.iOnlineDict = vm
                    onlineDict.initWebViewClient()
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    LollyAndroidTheme {
        SearchScreen() {}
    }
}
