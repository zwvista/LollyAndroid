package com.zwstudio.lolly.ui.misc

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.OnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.DrawerScreens
import com.zwstudio.lolly.ui.common.SearchView
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.common.TopBarMenu
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(openDrawer: () -> Unit) {

    val vm = koinViewModel<SearchViewModel>()
    val onlineDict = remember { OnlineDict() }
    val context = LocalContext.current
    // https://stackoverflow.com/questions/64181930/request-focus-on-textfield-in-jetpack-compose
    val focusRequester = remember { FocusRequester() }

    fun searchDict() {
        onlineDict.searchDict()
    }

    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
        if (GlobalUserViewModel.isLoggedIn) {
            vmSettings.getData()
            vmSettings.selectedDictReferenceIndex_.onEach {
                searchDict()
            }.launchIn(this)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBarMenu(
            title = DrawerScreens.Search.title,
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
            searchDict()
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text3))
                    .fillMaxWidth()
                    .weight(1f),
                itemsStateFlow = vmSettings.lstLanguages_,
                selectedItemIndexStateFlow = vmSettings.selectedLangIndex_,
                itemText = { it.langname }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text2))
                    .fillMaxWidth()
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
