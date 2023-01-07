package com.zwstudio.lolly.ui.misc

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.zwstudio.lolly.R
import com.zwstudio.lolly.common.OnlineDict
import com.zwstudio.lolly.common.vmSettings
import com.zwstudio.lolly.ui.common.Spinner
import com.zwstudio.lolly.ui.theme.LollyAndroidTheme
import com.zwstudio.lolly.viewmodels.misc.GlobalUserViewModel
import com.zwstudio.lolly.viewmodels.misc.SearchViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(openDrawer: () -> Unit) {

    val vm = getViewModel<SearchViewModel>()
    val onlineDict = remember { OnlineDict() }
    LaunchedEffect(Unit, block = {
        vmSettings.getData()
    })

    val context = LocalContext.current
    val (focusRequester) = FocusRequester.createRefs()
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
        Column(modifier = Modifier.fillMaxSize()) {
            TextField(
                value = vm.word_.collectAsState().value,
                onValueChange = { vm.word = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .onKeyEvent {
                        if (it.key == Key.Enter){
                            onlineDict.searchDict()
                            return@onKeyEvent true
                        }
                        false
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onlineDict.searchDict()
                    }
                ),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text3))
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .weight(1f),
                    dropDownModifier = Modifier.wrapContentSize(),
                    items = vmSettings.lstLanguages_.collectAsState().value,
                    selectedItemIndex = vmSettings.selectedLangIndex_.collectAsState().value,
                    onItemSelected = {},
                    selectedItemFactory = { modifier, _ ->
                        Row(
                            modifier = modifier
                                .padding(8.dp)
                                .wrapContentSize()
                        ) {
                            Text(
                                text = vmSettings.selectedLang.langname,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                                contentDescription ="drop down arrow"
                            )
                        }
                    },
                    dropdownItemFactory = { item, _ ->
                        Text(text = item.langname)
                    }
                )
                Spinner(
                    modifier = Modifier
                        .background(color = colorResource(R.color.color_text1))
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .weight(1f),
                    dropDownModifier = Modifier.wrapContentSize(),
                    items = vmSettings.lstDictsReference_.collectAsState().value,
                    selectedItemIndex = vmSettings.selectedDictReferenceIndex_.collectAsState().value,
                    onItemSelected = {},
                    selectedItemFactory = { modifier, _ ->
                        Row(
                            modifier = modifier
                                .padding(8.dp)
                                .wrapContentSize()
                        ) {
                            Text(
                                text = vmSettings.selectedDictReference.dictname,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_arrow_drop_down_24),
                                contentDescription ="drop down arrow"
                            )
                        }
                    },
                    dropdownItemFactory = { item, _ ->
                        Text(text = item.dictname)
                    }
                )
            }
            AndroidView(
                factory = {
                    WebView(it).apply {
                        onlineDict.wv = this
                        onlineDict.iOnlineDict = vm
                        onlineDict.initWebViewClient()
                        loadUrl("https://google.com")
                    }
                }, update = { webView ->
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {
    LollyAndroidTheme {
        SearchScreen() {}
    }
}
