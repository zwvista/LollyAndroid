package com.zwstudio.lolly.ui.misc

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.OnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.SearchView
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<SearchViewModel>()
    val onlineDict = remember { OnlineDict() }
    val context = LocalContext.current

    fun searchDict() {
        onlineDict.searchDict()
    }

    LaunchedEffect(Unit, block = {
        vmSettings.getData()
        vmSettings.selectedDictReferenceIndex_.onEach {
            searchDict()
        }.launchIn(this)
    })

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Search") },
            navigationIcon = {
                IconButton(onClick = { openDrawer() } ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            },
            actions = {
                IconButton(onClick = {
                    GlobalUserViewModel.remove(context)
                }) {
                    Icon(Icons.Filled.ExitToApp, null)
                }
            },
            backgroundColor = MaterialTheme.colors.primaryVariant
        )
        SearchView(valueStateFlow = vm.word_) {
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
                selectedItemText = { vmSettings.selectedLang.langname },
                dropdownItemText = { it.langname }
            )
            Spinner(
                modifier = Modifier
                    .background(color = colorResource(R.color.color_text1))
                    .fillMaxWidth()
                    .weight(1f),
                itemsStateFlow = vmSettings.lstDictsReference_,
                selectedItemIndexStateFlow = vmSettings.selectedDictReferenceIndex_,
                selectedItemText = { vmSettings.selectedDictReference.dictname },
                dropdownItemText = { it.dictname }
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
